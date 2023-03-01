package net.mrchar.zzplant.model;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "`shop_account`")
public class ShopAccount extends AbstractPersistable<UUID> {
    @Column(name = "code")
    private String code; // 用户在当前店铺开通的账户的编号
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password; // 顾客在当前商铺的付款密码
    @Column(name = "title")
    private String title; // 会员等级
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "balance")
    private BigDecimal balance; // 余额
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
}
