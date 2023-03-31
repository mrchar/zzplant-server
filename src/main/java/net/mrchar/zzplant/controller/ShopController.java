package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.ShopAssistant;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.ShopAssistantRepository;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.service.ShopService;
import net.mrchar.zzplant.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final ShopRepository shopRepository;
    private final ShopAssistantRepository assistantRepository;
    private final UserService userService;
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
        User user = this.userService.getCurrentOperator();
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
        User user = this.userService.getCurrentOperator();
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
        User owner = this.userService.getCurrentOperator();
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
        User owner = this.userService.getCurrentOperator();
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

}
