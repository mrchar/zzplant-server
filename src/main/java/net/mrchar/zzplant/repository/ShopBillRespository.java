package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.ShopBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopBillRespository extends JpaRepository<ShopBill, UUID> {
    /**
     * 获取商铺的所有商品
     *
     * @param shopCode
     * @param pageable
     * @return
     */
    @Query("select bill from ShopBill bill where bill.shop.code = :shopCode")
    Page<ShopBill> findAllByShopCode(String shopCode, Pageable pageable);

    /**
     * 搜索账单
     *
     * @param shopCode 商铺编号
     * @param keyword  搜索关键词
     * @param pageable 分页参数
     * @return 账单列表
     */
    @Query("select bill from ShopBill bill where bill.shop.code = :shopCode " +
            "and(" +
            "   bill.shopAccount.code = :keyword " +
            "   or bill.shopAccount.phoneNumber = :keyword " +
            "   or bill.code like :#{'%'+#keyword+'%'}" +
            ")")
    Page<ShopBill> searchBillByShopCodeAndKeyword(String shopCode, String keyword, Pageable pageable);

    /**
     * 列出商铺会员的所有账单
     *
     * @param shopCode    商铺编号
     * @param accountCode 会员编号
     * @param pageable    分页参数
     * @return 账单列表
     */
    @Query("select bill from ShopBill bill " +
            "where bill.shop.code = :shopCode and bill.shopAccount.code = :accountCode")
    Page<ShopBill> findAllByShopCodeAndAccountCode(String shopCode, String accountCode, Pageable pageable);

    /**
     * 查找商铺中指定订单
     *
     * @param shopCode 商铺编号
     * @param billCode 订单编号
     * @return 订单信息
     */
    Optional<ShopBill> findOneByShopCodeAndCode(String shopCode, String billCode);
}