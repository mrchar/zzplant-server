package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.ShopCommodity;
import net.mrchar.zzplant.model.ShopInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShopInvoiceRepository extends JpaRepository<ShopInvoice, UUID> {
    @Query("select invoice from ShopInvoice invoice where invoice.shop.code = :shopCode")
    Page<ShopInvoice> findAllByShopCode(String shopCode, Pageable pageable);


    @Query("select invoice from ShopInvoice invoice where invoice.shop.code = :shopCode and invoice.code in :commodityCodes")
    List<ShopCommodity> findAllByShopCodeAndCodeIn(String shopCode, Collection<String> commodityCodes);
}