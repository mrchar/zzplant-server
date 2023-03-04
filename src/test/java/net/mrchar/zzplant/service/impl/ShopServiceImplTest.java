package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.*;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.AccountService;
import net.mrchar.zzplant.service.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static net.mrchar.zzplant.model.Gender.FEMALE;
import static net.mrchar.zzplant.model.Gender.MALE;
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

    @Test
    void addShopAccount() {
        Account account = this.accountService.addAccount("15888888888", "password");
        User user = this.userRepository.findOneByAccountName(account.getName()).orElseThrow();

        String shopName = RandomStringUtils.randomAlphanumeric(10, 50);
        String address = RandomStringUtils.randomAlphanumeric(20, 100);
        Shop shop = this.shopService.addShop(shopName, address, user);

        String shopAccountName = RandomStringUtils.randomAlphanumeric(1, 10);
        Gender gender = Arrays.asList(MALE, FEMALE, null).get(RandomUtils.nextInt(0, 3));
        String shopAccountPhoneNumber = RandomStringUtils.randomNumeric(11);
        ShopAccount shopAccount = this.shopService.addShopAccount(shop.getCode(), shopAccountName, gender, shopAccountPhoneNumber);

        assertThat(shopAccount.getCode()).isNotBlank();
        assertThat(shopAccount.getName()).isEqualTo(shopAccountName);
        assertThat(shopAccount.getGender()).isEqualTo(gender);
        assertThat(shopAccount.getPhoneNumber()).isEqualTo(shopAccountPhoneNumber);
        assertThat(shopAccount.getShop().getCode()).isEqualTo(shop.getCode());
        assertThat(shopAccount.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void addInvoice() {
        Account account = this.accountService.addAccount("15888888888", "password");
        User user = this.userRepository.findOneByAccountName(account.getName()).orElseThrow();

        String shopName = RandomStringUtils.randomAlphanumeric(10, 50);
        String address = RandomStringUtils.randomAlphanumeric(20, 100);
        Shop shop = this.shopService.addShop(shopName, address, user);

        String shopAccountName = RandomStringUtils.randomAlphanumeric(1, 10);
        Gender gender = Arrays.asList(MALE, FEMALE, null).get(RandomUtils.nextInt(0, 3));
        String shopAccountPhoneNumber = RandomStringUtils.randomNumeric(11);
        ShopAccount shopAccount = this.shopService.addShopAccount(shop.getCode(), shopAccountName, gender, shopAccountPhoneNumber);

        // TODO: 添加商品

        ShopInvoice shopInvoice = this.shopService.addInvoice(shop.getCode(), account.getCode(), Map.of());

        // 添加断言
    }
}