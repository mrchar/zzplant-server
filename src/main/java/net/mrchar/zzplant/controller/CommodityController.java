package net.mrchar.zzplant.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommodityController {
    @Data
    public static class CommoditySchema {
        private String code;
        private String name;
        private BigDecimal price;
    }


    @GetMapping("/shops/{shopCode}/commodities")
    public List<CommoditySchema> listCommodities(
            @PathVariable String shopCode,
            @RequestParam(name = "off", required = false) boolean offShelf) {
        // 列出商品
        return Collections.emptyList();
    }

    @Data
    public static class AddCommodityRequest {
        private String name;
        private BigDecimal price;
    }


    @PostMapping("/shops/{shopCode}/commodities")
    public CommoditySchema addCommodity(@PathVariable String shopCode, @RequestBody AddCommodityRequest request) {
        // 创建商品
        return null;
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
