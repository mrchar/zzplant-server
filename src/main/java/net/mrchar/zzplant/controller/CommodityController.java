package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ForbiddenOperationException;
import net.mrchar.zzplant.model.ShopCommodity;
import net.mrchar.zzplant.repository.ShopCommodityRepository;
import net.mrchar.zzplant.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommodityController {
    private final ShopCommodityRepository commodityRepository;
    private final ShopService shopService;

    @Data
    public static class CommoditySchema {
        private String code;
        private String name;
        private BigDecimal price;

        public CommoditySchema(String code, String name, BigDecimal price) {
            this.code = code;
            this.name = name;
            this.price = price;
        }

        public static CommoditySchema fromEntity(ShopCommodity entity) {
            return new CommoditySchema(entity.getCode(), entity.getName(), entity.getPrice());
        }
    }


    @GetMapping("/shops/{shopCode}/commodities")
    public Page<CommoditySchema> listCommodities(
            @PathVariable String shopCode,
            @RequestParam(name = "off", required = false) Boolean offShelf,
            Pageable pageable) {
        boolean exists = this.shopService.operatorIsAssistant(shopCode);
        if (!exists) {
            throw new ForbiddenOperationException("您没有权限进行这个操作");
        }

        Page<ShopCommodity> commodities = this.commodityRepository
                .findAllByShopCode(shopCode, offShelf, pageable);
        return commodities.map(CommoditySchema::fromEntity);
    }

    @Data
    public static class AddCommodityRequest {
        private String name;
        private BigDecimal price;
    }


    @PostMapping("/shops/{shopCode}/commodities")
    public CommoditySchema addCommodity(@PathVariable String shopCode,
                                        @RequestBody AddCommodityRequest request) {
        boolean hasPermission = this.shopService.operatorIsAssistant(shopCode);
        if (!hasPermission) {
            throw new ForbiddenOperationException("您没有权限进行这个操作");
        }

        ShopCommodity entity = this.shopService
                .addShopCommodity(shopCode, request.getName(), request.getPrice());

        return CommoditySchema.fromEntity(entity);
    }


    @PutMapping("/shops/{shopCode}/commodities/{code}")
    public CommoditySchema updateCommodity(@PathVariable String shopCode,
                                           @PathVariable String code,
                                           @RequestBody AddCommodityRequest request) {
        // 更新商品
        return null;
    }


    @PostMapping("/shops/{shopCode}/commodities/{code}/off")
    public void setCommodityOffShelf(@PathVariable String shopCode, @PathVariable String code) {
        // 下架商品
    }


    @DeleteMapping("/shops/{shopCode}/commodities/{code}")
    public void deleteCommodity(@PathVariable String shopCode, @PathVariable String code) {
        // 删除商品
    }
}
