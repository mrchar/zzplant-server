package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`shop_invoice`")
public class ShopInvoice extends AbstractPersistable<UUID> {
    @Column(name = "code")
    private String code; // 流水号

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "shop_invoice_commodity",
            joinColumns = @JoinColumn(name = "invoice_id"))
    private Set<ShopInvoiceCommodity> commodities;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private ShopAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
}
