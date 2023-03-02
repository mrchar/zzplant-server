package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.UserDetailsImpl;
import net.mrchar.zzplant.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Account account = this.accountRepository.findOneByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return new UserDetailsImpl(account);
    }
}
