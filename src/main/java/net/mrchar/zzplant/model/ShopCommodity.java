package net.mrchar.zzplant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
}
