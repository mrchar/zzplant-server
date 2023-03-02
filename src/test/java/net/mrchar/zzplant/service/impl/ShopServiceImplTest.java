package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.AccountService;
import net.mrchar.zzplant.service.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShopServiceImplTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ShopService shopService;

    @Test
    @Transactional
    void addShop() {
        Account account = this.accountService.addAccount("15888888888", "password");
        User user = this.userRepository.findOneByAccountName(account.getName()).orElseThrow();

        String shopName = RandomStringUtils.randomAlphanumeric(10, 50);
        String address = RandomStringUtils.randomAlphanumeric(20, 100);
        Shop shop = this.shopService.addShop(shopName, address, user);

        shop = this.shopRepository.findOneByCode(shop.getCode()).orElseThrow();
        assertThat(shop.getName()).isEqualTo(shopName);
        assertThat(shop.getAddress()).isEqualTo(address);
        assertThat(shop.getOwner()).isEqualTo(user);
        assertThat(shop.getAssistants()).isNotEmpty();
        assertThat(shop.getAssistants().iterator().next().getCode()).isEqualTo("0001");
    }
}