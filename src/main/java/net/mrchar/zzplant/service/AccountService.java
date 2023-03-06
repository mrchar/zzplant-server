package net.mrchar.zzplant.service;

import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.Gender;


public interface AccountService {
    /**
     * 添加账户
     *
     * @param phoneNumber
     * @param password
     * @return
     */
    Account addAccount(String phoneNumber, String password);

    /**
     * 设置用户信息
     *
     * @param accountName 账户名
     * @param name        用户名
     * @param gender      性别
     * @return
     */
    Account setProfile(String accountName, String name, Gender gender);
}
