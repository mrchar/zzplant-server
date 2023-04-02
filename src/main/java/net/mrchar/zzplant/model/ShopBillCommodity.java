package net.mrchar.zzplant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class ShopBillCommodity {
    @Column(name = "code")
    private String code; // 商品编号

    @Column(name = "name")
    private String name; // 商品名称

    @Column(name = "price")
    private BigDecimal price; // 下单价格

    @Column(name = "quantity")
    private Integer quantity; // 下单数量

    @Column(name = "amount")
    private BigDecimal amount; // 费用总额

    public ShopBillCommodity() {
    }
}
