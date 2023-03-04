package net.mrchar.zzplant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
@Builder(setterPrefix = "with")
public class ShopInvoiceCommodity {
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity; // 数量

    @Column(name = "amount")
    private BigDecimal amount; // 总额

    public ShopInvoiceCommodity() {
    }
}
