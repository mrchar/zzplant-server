package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ResourceAlreadyExistsException;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.*;
import net.mrchar.zzplant.repository.*;
import net.mrchar.zzplant.service.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static net.mrchar.zzplant.model.ShopAccount.VIP;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private static final Integer SHOP_CODE_LENGTH = 10;
    private static final Integer SHOP_ACCOUNT_CODE_LENGTH = 10;

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShopAssistantRepository assistantRepository;
    private final ShopAccountRepository shopAccountRepository;

    @Override
    @Transactional
    public Shop addShop(String name, String address, User owner) {
        String code = RandomStringUtils.randomAlphanumeric(SHOP_CODE_LENGTH).toLowerCase();
        Shop shop = new Shop(code, name, address, owner);
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
    public ShopAccount addShopAccount(String shopCode, String name, Gender gender, String phoneNumber) {
        boolean exists = this.shopAccountRepository.existsByShopCodeAndPhoneNumber(shopCode, phoneNumber);
        if (exists) {
            throw new ResourceAlreadyExistsException("该手机号码已经绑定了该店铺的会员");
        }

        Shop shop = this.shopRepository.findOneByCode(shopCode)
                .orElseThrow(() -> new UnExpectedException("要操作的商铺不存在"));


        String code = RandomStringUtils.randomAlphanumeric(SHOP_ACCOUNT_CODE_LENGTH).toLowerCase();
        ShopAccount shopAccount = new ShopAccount(code, name, gender, VIP, phoneNumber, BigDecimal.ZERO, shop);
        this.shopAccountRepository.save(shopAccount);

        return shopAccount;
    }
}
