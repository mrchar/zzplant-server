package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ForbiddenOperationException;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.model.Gender;
import net.mrchar.zzplant.model.ShopAccount;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.ShopAccountRepository;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.service.ShopAccountService;
import net.mrchar.zzplant.service.ShopService;
import net.mrchar.zzplant.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopAccountController {
    private final ShopRepository shopRepository;
    private final ShopAccountRepository shopAccountRepository;
    private final UserService userService;
    private final ShopService shopService;
    private final ShopAccountService shopAccountService;

    @Data
    public static class ShopAccountSchema {
        private String code;
        private String name;
        private String gender;
        private String phoneNumber;
        private BigDecimal balance;
        private String shop;

        public ShopAccountSchema(String code, String name, String gender, String phoneNumber, BigDecimal balance) {
            this.code = code;
            this.name = name;
            this.gender = gender;
            this.phoneNumber = phoneNumber;
            this.balance = balance;
        }

        public static ShopAccountSchema fromEntity(ShopAccount entity) {
            String gender = "UNKNOWN";
            if (entity.getGender() != null) {
                gender = entity.getGender().toString();
            }
            ShopAccountSchema schema = new ShopAccountSchema(
                    entity.getCode(),
                    entity.getName(),
                    gender,
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
    public Page<ShopAccountSchema> listShopAccounts(@PathVariable String shopCode,
                                                    @RequestParam(required = false) String keyword,
                                                    Pageable pageable) {
        Page<ShopAccount> entities = null;
        if (StringUtils.hasText(keyword)) {
            entities = this.shopAccountRepository.searchShopAccountOfShop(shopCode, keyword, pageable);
        } else {
            entities = this.shopAccountRepository.findAllByShopCode(shopCode, pageable);
        }
        return entities.map(ShopAccountSchema::fromEntity);
    }

    @GetMapping("/shops/{shopCode}/accounts/{accountCode}")
    @Transactional
    public ShopAccountSchema getShopAccount(@PathVariable String shopCode, @PathVariable String accountCode) {
        ShopAccount entity = this.shopRepository.findOneByShopCodeAndCode(shopCode, accountCode)
                .orElseThrow(() -> new ResourceNotExistsException("会员不存在"));
        return ShopAccountSchema.fromEntity(entity);
    }

    @Data
    public static class AddShopAccountRequest {
        private String name;
        private String gender;
        private String phoneNumber;
        private BigDecimal balance;
    }

    @PostMapping("/shops/{shopCode}/accounts")
    public ShopAccountSchema addShopAccount(@PathVariable String shopCode,
                                            @RequestBody AddShopAccountRequest request) {
        User operator = this.userService.getCurrentOperator();
        boolean exists = this.shopRepository.existsByShopCodeAndOwner(shopCode, operator);
        if (!exists) {
            throw new ForbiddenOperationException("只有商铺的所有者可以创建会员");
        }


        ShopAccount entity = shopService.addShopAccount(
                shopCode, request.getName(),
                Gender.fromString(request.getGender()),
                request.getPhoneNumber(),
                request.getBalance());
        // 添加会员
        return ShopAccountSchema.fromEntity(entity);
    }

    @DeleteMapping("/shops/{shopCode}/accounts/{accountCode}")
    public ShopAccountSchema removeShopAccount(@PathVariable String shopCode,
                                               @PathVariable String accountCode) {
        // 移除会员
        return null;
    }


    @Data
    public static class TopUpRequest {
        private BigDecimal amount;
    }

    @PostMapping("/shop/{shopCode}/account/{accountCode}/top-up")
    public void topUp(@PathVariable String shopCode, @PathVariable String accountCode, @RequestBody TopUpRequest topUpRequest) {
        this.shopAccountService.topUp(shopCode, accountCode, topUpRequest.getAmount());
    }
}
