package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.ShopCommodity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopCommodityRepository extends JpaRepository<ShopCommodity, UUID> {
    /**
     * 获取指定商铺的商品列表
     *
     * @param shopCode 商铺编码
     * @param offShelf 上架状态
     * @param pageable 分页参数
     * @return 商品列表
     */
    @Query("select commodity from ShopCommodity commodity " +
            "where commodity.shop.code = :shopCode " +
            "and (:offShelf is null or commodity.offShelf = :offShelf)")
    Page<ShopCommodity> findAllByShopCode(String shopCode, Boolean offShelf, Pageable pageable);

    /**
     * 根据商品名称获取商品
     *
     * @param shop          商铺
     * @param commodityName 商品名称
     * @return 商品
     */
    @Query("select commodity from ShopCommodity commodity " +
            "where commodity.shop.id = :#{#shop.id} and commodity.name = :commodityName")
    Optional<ShopCommodity> findOneByShopAndName(Shop shop, String commodityName);

    /**
     * 查找商铺的商品列表
     *
     * @param shopCode
     * @param commodityCodes
     * @return
     */
    @Query("select commodity from ShopCommodity commodity where commodity.shop.code = :shopCode and commodity.code in :commodityCodes")
    List<ShopCommodity> findAllByShopCodeAndCodeIn(String shopCode, Collection<String> commodityCodes);

    /**
     * 根据商铺编号和商品编号查找商品
     *
     * @param shopCode      商铺编号
     * @param commodityCode 商品编号
     * @return 商品信息
     */
    @Query("select commodity from ShopCommodity commodity " +
            "where commodity.shop.code = :shopCode and commodity.code = :commodityCode")
    Optional<ShopCommodity> findOneByShopCodeAndCode(String shopCode, String commodityCode);
}