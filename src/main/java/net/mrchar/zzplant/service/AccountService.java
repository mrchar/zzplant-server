package net.mrchar.zzplant.service;

import net.mrchar.zzplant.model.Account;


public interface AccountService {
    /**
     * 添加账户
     *
     * @param phoneNumber
     * @param password
     * @return
     */
    Account addAccount(String phoneNumber, String password);
}
