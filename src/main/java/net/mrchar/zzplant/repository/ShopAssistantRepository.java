package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.ShopAssistant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopAssistantRepository extends JpaRepository<ShopAssistant, UUID> {
    /**
     * 查找店铺中的所有店员
     *
     * @param shopCode 店铺编号
     * @param pageable 分页参数
     * @return 店员列表
     */
    @Query("select assistant from ShopAssistant assistant where assistant.shop.code = :shopCode")
    Page<ShopAssistant> findAllByShopCode(String shopCode, Pageable pageable);


    /**
     * 判断用户是否是商户的店员
     *
     * @param shopCode    商铺编号
     * @param accountName 账户名称
     * @return 是否是店铺员工
     */
    @Query("select (count(1) > 0) from ShopAssistant assistant " +
            "where assistant.shop.code = :shopCode " +
            "and assistant.user.account.name = :accountName")
    boolean existsByShopCodeAndAccountName(String shopCode, String accountName);
}