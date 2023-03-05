package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.Gender;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.AccountRepository;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static net.mrchar.zzplant.model.Gender.FEMALE;
import static net.mrchar.zzplant.model.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceImplTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    MockAccountServiceTool mockAccountServiceTool;

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

    @Test
    @Transactional
    void setProfile() {
        Account account = this.mockAccountServiceTool.addAccount();

        String username = RandomStringUtils.randomAlphanumeric(5, 10);
        Gender gender = Arrays.asList(MALE, FEMALE, null).get(RandomUtils.nextInt(0, 3));
        this.accountService.setProfile(account.getName(), username, gender);

        User user = this.userRepository.findOneByAccountName(account.getName()).orElseThrow();
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(username);
        assertThat(user.getGender()).isEqualTo(gender);
    }
}