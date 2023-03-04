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
@Table(name = "`shop_account`")
public class ShopAccount extends AbstractPersistable<UUID> {
    public static final String VIP = "会员";

    @Column(name = "code")
    private String code; // 用户在当前店铺开通的账户的编号

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

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

    public ShopAccount() {
    }

    public ShopAccount(String code, String name, Gender gender, String title, String phoneNumber, BigDecimal balance, Shop shop) {
        this.code = code;
        this.name = name;
        this.gender = gender;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.shop = shop;
    }
}
