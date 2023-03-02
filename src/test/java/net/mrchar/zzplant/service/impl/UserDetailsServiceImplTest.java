package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserDetailsServiceImplTest {
    @Autowired
    AccountService accountService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void loadUserByUsername() {
        String phoneNumber = RandomStringUtils.randomNumeric(11);
        String password = RandomStringUtils.randomAlphanumeric(6, 20);
        this.accountService.addAccount(phoneNumber, password);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);
        assertThat(userDetails.getUsername()).isNotBlank();
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }
}