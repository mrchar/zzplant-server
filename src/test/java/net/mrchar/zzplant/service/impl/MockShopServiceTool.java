package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.AccountService;
import net.mrchar.zzplant.service.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockShopServiceTool {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ShopService shopService;

    Shop addShop() {
        Account account = this.accountService.addAccount("15888888888", "password");
        User user = this.userRepository.findOneByAccountName(account.getName()).orElseThrow();

        String shopName = RandomStringUtils.randomAlphanumeric(10, 50);
        String address = RandomStringUtils.randomAlphanumeric(20, 100);
        return this.shopService.addShop(shopName, address, user);
    }
}
