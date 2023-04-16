package net.mrchar.zzplant.service;

import lombok.Builder;
import lombok.Data;
import net.mrchar.zzplant.model.ShopCommodity;

import java.math.BigDecimal;

public interface ShopCommodityService {
    /**
     * 在商铺中添加商品
     *
     * @param shopCode
     * @param name
     * @param price
     * @return
     */
    ShopCommodity addShopCommodity(String shopCode, String name, BigDecimal price);


    @Data
    @Builder
    class UpdateShopCommodityOption {
        private String name;
        private BigDecimal price;
        private Boolean offShelf;
    }

    /**
     * 修改商品信息
     *
     * @param shopCode      商铺编号
     * @param commodityCode 商品名称
     * @param option        修改选项
     * @return 商品信息
     */
    ShopCommodity updateShopCommodity(String shopCode, String commodityCode, UpdateShopCommodityOption option);

    /**
     * 在商铺中删除商品
     *
     * @param shopCode      商铺编号
     * @param commodityCode 商品编号
     * @return 删除之前的商品信息
     */
    ShopCommodity deleteShopCommodity(String shopCode, String commodityCode);


}
