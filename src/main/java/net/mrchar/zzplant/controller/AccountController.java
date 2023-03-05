package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.Account;
import net.mrchar.zzplant.model.Gender;
import net.mrchar.zzplant.repository.AccountRepository;
import net.mrchar.zzplant.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
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
    public static class ProfileSchema {
        private String accountCode;
        private String accountName;
        private String name;
        private Gender gender;
        private String phoneNumber;

        public ProfileSchema() {
        }

        public ProfileSchema(String accountCode, String accountName) {
            this.accountCode = accountCode;
            this.accountName = accountName;
        }

        public static ProfileSchema fromEntity(Account entity) {
            ProfileSchema profileSchema = new ProfileSchema(entity.getCode(), entity.getName());
            if (entity.getUser() != null) {
                profileSchema.setName(entity.getUser().getName());
                profileSchema.setGender(entity.getUser().getGender());
                profileSchema.setPhoneNumber(entity.getUser().getPhoneNumber());
            }
            return profileSchema;
        }
    }

    @GetMapping("/self/profile")
    @Transactional
    public ProfileSchema getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnExpectedException("无法获取用户身份");
        }

        String accountName = authentication.getName();
        Account account = this.accountRepository.findOneByName(accountName)
                .orElseThrow(() -> new UnExpectedException("找不到账户"));

        return ProfileSchema.fromEntity(account);
    }

    @Data
    public static class SetProfileRequest {
        private String name;
        private String gender;
    }

    @PostMapping("/self/profile")
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
