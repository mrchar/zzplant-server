package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.controller.ShopController;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    /**
     * 查找工号最大的店员
     *
     * @param shop
     * @return
     */
    @Query("select assistant.code from ShopAssistant assistant " +
            "where assistant.shop.id = :#{#shop.id} " +
            "order by assistant.code desc limit 1")
    String findMaxAssistantCode(Shop shop);

    /**
     * 根据商铺编号查找商铺
     *
     * @param code 商铺编号
     * @return 商铺信息
     */
    @Query("select shop from Shop shop where shop.code = :code")
    Optional<Shop> findOneByCode(String code);

    /**
     * 获取用户所有者的所有商铺
     *
     * @param user 用户
     * @return 拥有的商铺列表
     */
    @Query("select shop from Shop shop where shop.owner.id = :#{#user.id}")
    Page<ShopController.ShopSchema> findAllByOwner(User user, Pageable pageable);

    /**
     * 查找用户所属的商铺是否存在
     *
     * @param shopCode 商铺编号
     * @param owner    商铺所有者
     * @return 商铺信息
     */
    @Query("select (count(1) > 0) from Shop shop where shop.code = :shopCode and shop.owner.id = :#{#owner.id}")
    boolean existsByShopCodeAndOwner(String shopCode, User owner);
}