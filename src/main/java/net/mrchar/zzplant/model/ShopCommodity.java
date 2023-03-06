package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`shop_commodity`")
public class ShopCommodity extends AbstractPersistable<UUID> {
    private static final Integer COMMODITY_CODE_LENGTH = 8;

    @Column(name = "code")
    private String code; // 商品编号

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "off_shelf")
    private boolean offShelf;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public ShopCommodity() {
    }

    public ShopCommodity(String name, BigDecimal price, boolean offShelf, Shop shop) {
        this.name = name;
        this.price = price;
        this.offShelf = offShelf;
        this.shop = shop;
    }

    @PrePersist
    public void init() {
        this.code = RandomStringUtils.randomAlphanumeric(COMMODITY_CODE_LENGTH);
    }
}
