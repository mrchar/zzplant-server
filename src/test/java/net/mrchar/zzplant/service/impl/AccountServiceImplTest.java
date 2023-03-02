package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceImplTest {
    @Autowired
    AccountService accountService;

    @Test
    @Transactional
    void addAccount() {
        String phoneNumber = RandomStringUtils.randomNumeric(11);
        String password = RandomStringUtils.randomAlphanumeric(6, 20);
        Account account = this.accountService.addAccount(phoneNumber, password);
        assertThat(account.getCode()).isNotBlank();
        assertThat(account.getName()).isNotBlank();
        assertThat(account.getPassword()).isNotBlank().isNotEqualTo(password);
    }
}