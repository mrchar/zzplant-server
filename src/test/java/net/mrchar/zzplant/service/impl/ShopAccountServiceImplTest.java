package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.Gender;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.ShopAccount;
import net.mrchar.zzplant.repository.ShopAccountRepository;
import net.mrchar.zzplant.service.ShopAccountService;
import net.mrchar.zzplant.service.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

import static net.mrchar.zzplant.model.Gender.FEMALE;
import static net.mrchar.zzplant.model.Gender.MALE;

@SpringBootTest
class ShopAccountServiceImplTest {
    @Autowired
    MockShopServiceTool mockShopServiceTool;
    @Autowired
    ShopService shopService;
    @Autowired
    ShopAccountService shopAccountService;
    @Autowired
    ShopAccountRepository shopAccountRepository;

    @Test
    @Transactional
    void topUp() {
        Shop shop = this.mockShopServiceTool.addShop();

        String shopAccountName = RandomStringUtils.randomAlphanumeric(1, 10);
        Gender gender = Arrays.asList(MALE, FEMALE, null).get(RandomUtils.nextInt(0, 3));
        String shopAccountPhoneNumber = RandomStringUtils.randomNumeric(11);
        ShopAccount shopAccount = this.shopService.addShopAccount(shop.getCode(), shopAccountName, gender, shopAccountPhoneNumber, BigDecimal.ZERO);


        this.shopAccountService.topUp(shop.getCode(), shopAccount.getCode(), BigDecimal.valueOf(100));
        Assertions.assertThat(shopAccount.getBalance()).isEqualTo(BigDecimal.valueOf(100));
    }
}