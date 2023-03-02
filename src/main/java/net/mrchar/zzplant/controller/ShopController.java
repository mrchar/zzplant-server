package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.ShopSchema;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.ShopService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final UserRepository userRepository;
    private final ShopService shopService;

    @GetMapping("/shops")
    public List<ShopSchema> listShops() {
        // 获取商铺列表
        return Collections.emptyList();
    }

    @Data
    public static class SetShopRequest {
        private String name;
        private String address;
    }

    @PostMapping("/shops")
    public ShopSchema addShop(@RequestBody SetShopRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnExpectedException("没有找到账户信息，请重新登录");
        }

        String accountName = authentication.getName();
        if (accountName == null) {
            throw new UnExpectedException("没有找到账户信息，请重新登录");
        }

        User user = this.userRepository.findOneByAccountName(accountName)
                .orElseThrow(() -> new UnExpectedException("找不到当前账户对应的用户信息"));

        Shop shop = this.shopService.addShop(request.getName(), request.getAddress(), user);
        return ShopSchema.fromEntity(shop);
    }

    @PutMapping("/shops/{shopCode}")
    public ShopSchema updateShop(@PathVariable String shopCode, // 商铺编号
                                 @RequestBody SetShopRequest request) {
        // 设置商铺
        return null;
    }

    @Data
    public static class ShopAssistantSchema {
        private String name;
        private String phoneNumber;
        private String shopCode;
        private String shopName;
    }

    @GetMapping("/shops/{shopCode}/assistants")
    public List<ShopAssistantSchema> listShopAssistants(@PathVariable String shopCode) {
        // 列出商店的所有员工
        return Collections.emptyList();
    }

    @Data
    public static class AddShopAssistant {
        private String username;
        private String phoneNumber;
    }


    @PostMapping("/shops/{shopCode}/assistant")
    public ShopAssistantSchema addShopAssistant(@PathVariable String shopCode, // 商铺编号
                                                @RequestBody AddShopAssistant request) {
        // 添加店员
        return null;
    }


    @DeleteMapping("/shops/{shopCode}/assistants/{AssistantCode}")
    public ShopAssistantSchema removeAssistant(@PathVariable String shopCode,
                                               @PathVariable String AssistantCode) {
        // 移除店员
        return null;
    }

    @Data
    public static class ShopAccountSchema {
        private String code;
        private String name;
        private String phoneNumber;
        private BigDecimal balance;
        private String shop;
    }


    @GetMapping("/shops/{shopCode}/accounts")
    public List<ShopAccountSchema> listShopAccounts(@PathVariable String shopCode) {
        return Collections.emptyList();
    }

    @Data
    public static class AddShopAccountRequest {
        private String name;
        private String gender;
        private String phoneNumber;
    }

    @PostMapping("/shops/{shopCode}/accounts")
    public ShopAccountSchema addShopAccount(@PathVariable String shopCode,
                                            @RequestBody AddShopAccountRequest request) {
        // 添加会员
        return null;
    }

    @DeleteMapping("/shops/{shopCode}/accounts/{accountCode}")
    public ShopAccountSchema removeShopAccount(@PathVariable String shopCode,
                                               @PathVariable String accountCode) {
        // 移除会员
        return null;
    }
}
