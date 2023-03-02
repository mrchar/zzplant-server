package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.Gender;
import net.mrchar.zzplant.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Data
    public static class RegisterRequest {
        private String phoneNumber;
        private String password;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
        this.accountService.addAccount(request.getPhoneNumber(), request.getPassword());
    }

    @Data
    public static class SetProfileRequest {
        private String name;
        private String gender;
    }

    @PostMapping("/profile")
    public void setProfile(@RequestBody SetProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnExpectedException("无法获取用户身份");
        }

        String accountName = authentication.getName();
        String username = request.getName();
        Gender gender = Gender.fromString(request.getGender());
        this.accountService.setProfile(accountName, username, gender);
    }
}
