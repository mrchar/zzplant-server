package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.ShopTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopTransactionRepository extends JpaRepository<ShopTransaction, UUID> {
}
