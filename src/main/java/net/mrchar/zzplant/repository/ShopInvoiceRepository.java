package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.ShopInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopInvoiceRepository extends JpaRepository<ShopInvoice, UUID> {
}