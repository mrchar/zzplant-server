package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.Gender;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.AccountRepository;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account addAccount(String phoneNumber, String password) {
        String accountName = phoneNumber + RandomStringUtils.randomAlphanumeric(4).toLowerCase();
        password = this.passwordEncoder.encode(password);

        Account account = new Account(accountName, password);
        this.accountRepository.save(account);

        User user = new User("user_" + phoneNumber, null, phoneNumber, account);
        this.userRepository.save(user);

        return account;
    }

    @Override
    public Account setProfile(String accountName, String name, Gender gender) {
        User user = this.userRepository.findOneByAccountName(accountName)
                .orElseThrow(() -> new UnExpectedException("账户不存在"));

        user.setName(name);
        user.setGender(gender);
        this.userRepository.save(user);
        return user.getAccount();
    }
}
