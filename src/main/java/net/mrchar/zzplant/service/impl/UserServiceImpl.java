package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getCurrentOperator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnExpectedException("没有找到账户信息，请重新登录");
        }

        String accountName = authentication.getName();
        if (accountName == null) {
            throw new UnExpectedException("没有找到账户信息，请重新登录");
        }

        return this.userRepository.findOneByAccountName(accountName)
                .orElseThrow(() -> new UnExpectedException("找不到当前账户对应的用户信息"));

    }
}
