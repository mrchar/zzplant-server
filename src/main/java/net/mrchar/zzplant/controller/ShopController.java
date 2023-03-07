package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ForbiddenOperationException;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.*;
import net.mrchar.zzplant.repository.ShopAccountRepository;
import net.mrchar.zzplant.repository.ShopAssistantRepository;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShopAssistantRepository assistantRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final ShopService shopService;

    @Data
    public static class ShopSchema {
        private String code;
        private String name;
        private String address;
        private String owner;
        private String company;

        public ShopSchema() {
        }

        public ShopSchema(String code, String name, String address) {
            this.code = code;
            this.name = name;
            this.address = address;
            this.owner = owner;
        }

        public static ShopSchema fromEntity(Shop entity) {
            ShopSchema schema = new ShopSchema(
                    entity.getCode(),
                    entity.getName(),
                    entity.getAddress());
            if (entity.getOwner() != null) {
                schema.setOwner(entity.getOwner().getName());
            }
            if (entity.getCompany() != null) {
                schema.setCompany(entity.getCompany().getName());
            }
            return schema;
        }
    }

    @GetMapping("/shops")
    @Transactional
    public Page<ShopSchema> listShops(Pageable pageable) {
        User user = this.getCurrentOperator();
        Page<Shop> entities = this.shopRepository.findAllByOwner(user, pageable);
        return entities.map(ShopSchema::fromEntity);
    }

    @Data
    public static class SetShopRequest {
        private String name;
        private String address;
    }

    @PostMapping("/shops")
    public ShopSchema addShop(@RequestBody SetShopRequest request) {
        User user = getCurrentOperator();
        Shop shop = this.shopService.addShop(request.getName(), request.getAddress(), user);
        return ShopSchema.fromEntity(shop);
    }

    private User getCurrentOperator() {
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

        public ShopAssistantSchema(String name, String phoneNumber, String shopCode, String shopName) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.shopCode = shopCode;
            this.shopName = shopName;
        }

        public static ShopAssistantSchema fromEntity(ShopAssistant entity) {
            return new ShopAssistantSchema(
                    entity.getName(),
                    entity.getUser().getPhoneNumber(),
                    entity.getShop().getCode(),
                    entity.getShop().getName());
        }
    }

    @GetMapping("/shops/{shopCode}/assistants")
    @Transactional
    public Page<ShopAssistantSchema> listShopAssistants(@PathVariable String shopCode, Pageable pageable) {
        User owner = this.getCurrentOperator();
        boolean exists = this.shopRepository.existsByShopCodeAndOwner(shopCode, owner);
        if (!exists) {
            throw new ResourceNotExistsException("商铺不存在");
        }

        Page<ShopAssistant> entities = this.assistantRepository.findAllByShopCode(shopCode, pageable);

        // 列出商店的所有员工
        return entities.map(ShopAssistantSchema::fromEntity);
    }

    @Data
    public static class AddShopAssistant {
        private String username;
        private String phoneNumber;
    }


    @PostMapping("/shops/{shopCode}/assistant")
    @Transactional
    public ShopAssistantSchema addShopAssistant(@PathVariable String shopCode, // 商铺编号
                                                @RequestBody AddShopAssistant request) {
        User owner = this.getCurrentOperator();
        boolean exists = this.shopRepository.existsByShopCodeAndOwner(shopCode, owner);
        if (!exists) {
            throw new ResourceNotExistsException("商铺不存在");
        }

        ShopAssistant entity = this.shopService
                .addShopAssistant(shopCode, request.getUsername(), request.getPhoneNumber());
        return ShopAssistantSchema.fromEntity(entity);
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

        public ShopAccountSchema(String code, String name, String phoneNumber, BigDecimal balance) {
            this.code = code;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.balance = balance;
        }

        public static ShopAccountSchema fromEntity(ShopAccount entity) {
            ShopAccountSchema schema = new ShopAccountSchema(
                    entity.getCode(),
                    entity.getName(),
                    entity.getPhoneNumber(),
                    entity.getBalance()
            );

            if (entity.getShop() != null) {
                schema.setShop(entity.getShop().getCode());
            }

            return schema;
        }
    }


    @GetMapping("/shops/{shopCode}/accounts")
    @Transactional
    public Page<ShopAccountSchema> listShopAccounts(@PathVariable String shopCode, Pageable pageable) {
        Page<ShopAccount> entities = this.shopAccountRepository.findAllByShopCode(shopCode, pageable);
        return entities.map(ShopAccountSchema::fromEntity);
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
        User operator = this.getCurrentOperator();
        boolean exists = this.shopRepository.existsByShopCodeAndOwner(shopCode, operator);
        if (!exists) {
            throw new ForbiddenOperationException("只有商铺的所有者可以创建会员");
        }


        ShopAccount entity = shopService.addShopAccount(
                shopCode, request.getName(),
                Gender.fromString(request.getGender()),
                request.getPhoneNumber());
        // 添加会员
        return ShopAccountSchema.fromEntity(entity);
    }

    @DeleteMapping("/shops/{shopCode}/accounts/{accountCode}")
    public ShopAccountSchema removeShopAccount(@PathVariable String shopCode,
                                               @PathVariable String accountCode) {
        // 移除会员
        return null;
    }
}
