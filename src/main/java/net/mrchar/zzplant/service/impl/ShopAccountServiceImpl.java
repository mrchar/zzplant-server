package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.model.ShopAccount;
import net.mrchar.zzplant.model.ShopTransaction;
import net.mrchar.zzplant.repository.ShopAccountRepository;
import net.mrchar.zzplant.repository.ShopTransactionRepository;
import net.mrchar.zzplant.service.ShopAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static net.mrchar.zzplant.model.ShopTransaction.Type.TOP_UP;

@Service
@RequiredArgsConstructor
public class ShopAccountServiceImpl implements ShopAccountService {
    private final ShopAccountRepository shopAccountRepository;
    private final ShopTransactionRepository shopTransactionRepository;

    @Override
    @Transactional
    public void topUp(String shopCode, String accountCode, BigDecimal amount) {
        ShopAccount shopAccount = this.shopAccountRepository.findOneByShopCodeAndCode(shopCode, accountCode)
                .orElseThrow(() -> new ResourceNotExistsException("账户不存在"));

        shopAccount = this.shopAccountRepository.findOneByIdForUpdate(shopAccount.getId())
                .orElseThrow(() -> new ResourceNotExistsException("账户不存在"));

        ShopTransaction transaction = new ShopTransaction(shopAccount, TOP_UP, amount);
        this.shopTransactionRepository.save(transaction);

        shopAccount.setBalance(transaction.getCurrentBalance());
        this.shopAccountRepository.save(shopAccount);
    }
}
