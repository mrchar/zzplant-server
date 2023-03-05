package net.mrchar.zzplant.service.impl;

import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockAccountServiceTool {
    @Autowired
    AccountService accountService;


    public Account addAccount() {
        String phoneNumber = RandomStringUtils.randomNumeric(11);
        String password = RandomStringUtils.randomAlphanumeric(6, 20);
        return this.accountService.addAccount(phoneNumber, password);
    }
}
