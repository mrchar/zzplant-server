package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "shop_transaction")
public class ShopTransaction extends AbstractPersistable<UUID> {
    private static final int CODE_LENGTH = 10;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "shop_account_id")
    private ShopAccount shopAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "previous_balance")
    private BigDecimal previousBalance;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    public ShopTransaction() {
    }

    public ShopTransaction(
            ShopAccount shopAccount,
            Type type, BigDecimal amount) {
        this.shop = shopAccount.getShop();
        this.shopAccount = shopAccount;
        this.type = type;
        this.amount = amount;
        this.previousBalance = shopAccount.getBalance();
        this.currentBalance = shopAccount.getBalance().add(amount);
    }

    @PrePersist
    public void init() {
        this.code = RandomStringUtils.randomAlphanumeric(CODE_LENGTH).toLowerCase();
    }

    public enum Type {
        TOP_UP, PAYMENT
    }
}
