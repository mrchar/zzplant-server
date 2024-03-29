package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`shop_bill`")
public class ShopBill extends AbstractPersistable<UUID> {
    private static final int CODE_LENGTH = 16;

    @Column(name = "code")
    private String code; // 流水号

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_account_id")
    private ShopAccount shopAccount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "shop_bill_commodity",
            joinColumns = @JoinColumn(name = "bill_id"))
    private Set<ShopBillCommodity> commodities;

    @Column(name = "amount")
    private BigDecimal amount; // 商品总值

    @OneToOne
    @JoinColumn(name = "shop_transaction_id")
    private ShopTransaction shopTransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id")
    private ShopAssistant operator;

    @Column(name = "createDateTime")
    private ZonedDateTime createDateTime;

    public ShopBill() {
    }

    public ShopBill(Shop shop,
                    ShopAccount shopAccount,
                    Set<ShopBillCommodity> commodities,
                    BigDecimal amount,
                    ShopAssistant operator) {
        this.shop = shop;
        this.shopAccount = shopAccount;
        this.commodities = commodities;
        this.amount = amount;
        this.operator = operator;
    }

    @PrePersist
    public void init() {
        this.code = RandomStringUtils
                .randomAlphanumeric(CODE_LENGTH)
                .toLowerCase();
        this.createDateTime = ZonedDateTime.now();
    }
}
