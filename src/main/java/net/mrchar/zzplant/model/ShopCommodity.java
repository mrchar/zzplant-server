package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`shop_commodity`")
public class ShopCommodity extends AbstractPersistable<UUID> {
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

    public ShopCommodity(String code, String name, BigDecimal price, boolean offShelf, Shop shop) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.offShelf = offShelf;
        this.shop = shop;
    }
}
