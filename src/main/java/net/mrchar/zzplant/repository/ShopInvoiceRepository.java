package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.ShopInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopInvoiceRepository extends JpaRepository<ShopInvoice, UUID> {
    /**
     * 获取商铺的所有商品
     *
     * @param shopCode
     * @param pageable
     * @return
     */
    @Query("select invoice from ShopInvoice invoice where invoice.shop.code = :shopCode")
    Page<ShopInvoice> findAllByShopCode(String shopCode, Pageable pageable);

    /**
     * 搜索账单
     *
     * @param shopCode 商铺编号
     * @param keyword  搜索关键词
     * @param pageable 分页参数
     * @return 账单列表
     */
    @Query("select invoice from ShopInvoice invoice where invoice.shop.code = :shopCode " +
            "and(" +
            "   invoice.account.code = :keyword " +
            "   or invoice.account.phoneNumber = :keyword " +
            "   or invoice.code like :#{'%'+#keyword+'%'}" +
            ")")
    Page<ShopInvoice> searchInvoiceByShopCodeAndKeyword(String shopCode, String keyword, Pageable pageable);

    /**
     * 列出商铺会员的所有账单
     *
     * @param shopCode    商铺编号
     * @param accountCode 会员编号
     * @param pageable    分页参数
     * @return 账单列表
     */
    @Query("select invoice from ShopInvoice invoice " +
            "where invoice.shop.code = :shopCode and invoice.account.code = :accountCode")
    Page<ShopInvoice> findAllByShopCodeAndAccountCode(String shopCode, String accountCode, Pageable pageable);
}