package net.mrchar.zzplant.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Data
    public static class RegisterRequest {
        private String phoneNumber;
        private String password;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
        // 创建账户
    }

    @Data
    public static class SetProfileRequest {
        private String name;
        private String gender;
        private String phoneNumber;
    }

    @PostMapping("/profile")
    public void setProfile(@RequestBody SetProfileRequest request) {
        // 设置用户信息
    }
}
