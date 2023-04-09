package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.BalanceIsNotEnoughException;
import net.mrchar.zzplant.exception.ResourceAlreadyExistsException;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.*;
import net.mrchar.zzplant.model.ShopTransaction.Type;
import net.mrchar.zzplant.repository.*;
import net.mrchar.zzplant.service.ShopService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static net.mrchar.zzplant.model.ShopAccount.VIP;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShopAssistantRepository assistantRepository;
    private final ShopCommodityRepository commodityRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final ShopBillRespository shopBillRespository;
    private final ShopTransactionRepository shopTransactionRepository;

    @Override
    @Transactional
    public Shop addShop(String name, String address, User owner) {

        Shop shop = new Shop(name, address, owner);
        this.shopRepository.save(shop);

        ShopAssistant assistant = new ShopAssistant(String.format("%04d", 1),
                owner.getName(), "经理", owner, shop);
        this.assistantRepository.save(assistant);

        shop.setAssistants(Set.of(assistant));
        return shop;
    }

    @Override
    @Transactional
    public ShopAssistant addShopAssistant(String shopCode, String username, String phoneNumber) {
        Shop shop = this.shopRepository.findOneByCode(shopCode)
                .orElseThrow(() -> new IllegalStateException("商铺不存在"));

        // 检查手机号对应的账户是否存在
        Optional<Account> optional = this.accountRepository.findOneByPhoneNumber(phoneNumber);
        if (optional.isPresent()) {
            // TODO: 发送加入店铺的邀请
            throw new UnExpectedException("手机号已经绑定了可以登录的账户");
        }

        User user = new User(username, phoneNumber);
        this.userRepository.save(user);

        String code = this.shopRepository.findMaxAssistantCode(shop);
        code = String.format("%04d", Integer.parseInt(code) + 1);
        ShopAssistant assistant = new ShopAssistant(code, username, "店员", user, shop);
        this.assistantRepository.save(assistant);

        return assistant;
    }

    @Override
    @Transactional
    public ShopCommodity addShopCommodity(String shopCode, String name, BigDecimal price) {
        Shop shop = this.shopRepository.findOneByCode(shopCode)
                .orElseThrow(() -> new ResourceNotExistsException("店铺不存在"));

        this.commodityRepository.findOneByShopAndName(shop, name)
                .ifPresent((commodity) -> {
                    throw new ResourceAlreadyExistsException("商品名称不能重复");
                });

        ShopCommodity shopCommodity = new ShopCommodity(name, price, false, shop);
        return this.commodityRepository.save(shopCommodity);
    }

    @Override
    @Transactional
    public ShopAccount addShopAccount(String shopCode, String name, Gender gender, String phoneNumber, BigDecimal balance) {
        boolean exists = this.shopAccountRepository.existsByShopCodeAndPhoneNumber(shopCode, phoneNumber);
        if (exists) {
            throw new ResourceAlreadyExistsException("该手机号码已经绑定了该店铺的会员");
        }

        Shop shop = this.shopRepository.findOneByCode(shopCode)
                .orElseThrow(() -> new UnExpectedException("要操作的商铺不存在"));

        ShopAccount shopAccount = new ShopAccount(name, gender, VIP, phoneNumber, balance, shop);
        this.shopAccountRepository.save(shopAccount);

        return shopAccount;
    }


    @Override
    @Transactional
    public ShopBill addBill(String shopCode, String accountCode, Map<String, Integer> commodities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UnExpectedException("查询不到当前正在操作的店员信息");
        }

        ShopAssistant operator = this.assistantRepository
                .findOneByShopCodeAndAccountName(shopCode, authentication.getName())
                .orElseThrow(() -> new UnExpectedException("查询不到当前正在操作的店员信息"));

        Shop shop = this.shopRepository.findOneByCode(shopCode)
                .orElseThrow(() -> new ResourceNotExistsException("店铺不存在"));

        ShopAccount shopAccount = this.shopAccountRepository.findOneByShopCodeAndCode(shopCode, accountCode)
                .orElseThrow(() -> new ResourceNotExistsException("会员不存在"));

        Set<ShopBillCommodity> shopBillCommodities = generateShopBillCommodities(shopCode, commodities);
        // 计算账单总价
        BigDecimal amount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(shopBillCommodities)) {
            amount = shopBillCommodities.stream()
                    .reduce(BigDecimal.ZERO, (sum, commodity) -> sum.add(commodity.getAmount()), null);
        }

        ShopBill shopBill = new ShopBill(shop, shopAccount, shopBillCommodities, amount, operator);
        return this.shopBillRespository.save(shopBill);
    }

    private Set<ShopBillCommodity> generateShopBillCommodities(String shopCode, Map<String, Integer> commodities) {
        List<ShopCommodity> commodityEntities = this.commodityRepository
                .findAllByShopCodeAndCodeIn(shopCode, commodities.keySet());
        return commodityEntities.stream()
                .map(entity -> {
                    // 获取商品数量
                    Integer quantity = commodities.get(entity.getCode());
                    // 获取当前商品总价
                    BigDecimal amount = entity.getPrice().multiply(BigDecimal.valueOf(quantity));
                    return ShopBillCommodity
                            .builder()
                            .withCode(entity.getCode())
                            .withName(entity.getName())
                            .withPrice(entity.getPrice())
                            .withQuantity(quantity)
                            .withAmount(amount)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public ShopBill modifyBill(String shopCode, String billCode, Map<String, Integer> commodities) {
        ShopBill entity = this.shopBillRespository.findOneByShopCodeAndCode(shopCode, billCode)
                .orElseThrow(() -> new ResourceNotExistsException("订单不存在"));


        Set<ShopBillCommodity> shopBillCommodities = generateShopBillCommodities(shopCode, commodities);
        // 计算账单总价
        BigDecimal amount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(shopBillCommodities)) {
            amount = shopBillCommodities.stream()
                    .map(ShopBillCommodity::getAmount)
                    .reduce(BigDecimal.ZERO, (sum, item) -> sum.add(item));
        }

        entity.setCommodities(shopBillCommodities);
        entity.setAmount(amount);
        this.shopBillRespository.save(entity);

        return entity;
    }


    @Override
    @Transactional
    public void deleteBill(String shopCode, String billCode) {
        ShopBill entity = this.shopBillRespository.findOneByShopCodeAndCode(shopCode, billCode)
                .orElseThrow(() -> new ResourceNotExistsException("订单不存在"));

        this.shopBillRespository.deleteById(entity.getId());
    }


    @Override
    @Transactional
    public ShopBill confirmBill(String shopCode, String billCode, Map<String, Integer> commodities, BigDecimal amount) {
        ShopBill entity = this.modifyBill(shopCode, billCode, commodities);

        // 添加shopAccount表行锁
        ShopAccount shopAccount = entity.getShopAccount();
        shopAccount = this.shopAccountRepository.findOneByIdForUpdate(shopAccount.getId())
                .orElseThrow(() -> new UnExpectedException("添加商铺账户行锁时失败"));

        // 创建交易
        if (shopAccount.getBalance().add(amount.negate()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceIsNotEnoughException("账户余额不足以支付订单");
        }
        ShopTransaction transaction = new ShopTransaction(entity.getShopAccount(), Type.PAYMENT, amount.negate());
        this.shopTransactionRepository.save(transaction);

        // 关联订单和交易
        entity.setShopTransaction(transaction);
        this.shopBillRespository.save(entity);

        // 修改剩余金额
        shopAccount.setBalance(transaction.getCurrentBalance());
        this.shopAccountRepository.save(shopAccount);

        return entity;
    }

    @Override
    public boolean operatorIsAssistant(String shopCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return false;
        }

        Optional<ShopAssistant> optional = this.assistantRepository
                .findOneByShopCodeAndAccountName(shopCode, authentication.getName());

        return optional.isPresent();
    }
}
