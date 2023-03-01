package net.mrchar.zzplant.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {
    @Data
    public static class ShopSchema {
        private String code;
        private String name;
        private String address;
        private String owner;
        private String company;
    }

    @GetMapping("/shop")
    public List<ShopSchema> listShops() {
        // 获取商铺列表
        return Collections.emptyList();
    }

    @Data
    public static class SetShopRequest {
        private String name;
        private String address;
    }

    @PostMapping("/shop")
    public ShopSchema addShop(@RequestBody SetShopRequest request) {
        // 创建商铺
        return null;
    }

    @PutMapping("/shop/{shopCode}")
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

    @GetMapping("/shop/{shopCode}/assistants")
    public List<ShopAssistantSchema> listShopAssistants(@PathVariable String shopCode) {
        // 列出商店的所有员工
        return Collections.emptyList();
    }

    @Data
    public static class AddShopAssistant {
        private String username;
        private String phoneNumber;
    }


    @PostMapping("/shop/{shopCode}/assistant")
    public ShopAssistantSchema addShopAssistant(@PathVariable String shopCode, // 商铺编号
                                                @RequestBody AddShopAssistant request) {
        // 添加店员
        return null;
    }


    @DeleteMapping("/shop/{shopCode}/assistants/{AssistantCode}")
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


    @GetMapping("/shop/{shopCode}/account")
    public List<ShopAccountSchema> listShopAccounts(@PathVariable String shopCode) {
        return Collections.emptyList();
    }

    @Data
    public static class AddShopAccountRequest {
        private String name;
        private String gender;
        private String phoneNumber;
    }

    @PostMapping("/shop/{shopCode}/account")
    public ShopAccountSchema addShopAccount(@PathVariable String shopCode,
                                            @RequestBody AddShopAccountRequest request) {
        // 添加会员
        return null;
    }

    @DeleteMapping("/shop/{shopCode}/account/{accountCode}")
    public ShopAccountSchema removeShopAccount(@PathVariable String shopCode,
                                               @PathVariable String accountCode) {
        // 移除会员
        return null;
    }
}
