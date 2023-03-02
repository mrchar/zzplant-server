package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.Shop;
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
}