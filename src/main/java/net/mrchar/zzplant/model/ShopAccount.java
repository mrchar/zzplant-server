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
@Table(name = "`shop_account`")
public class ShopAccount extends AbstractPersistable<UUID> {
    private static final Integer SHOP_ACCOUNT_CODE_LENGTH = 10;
    public static final String VIP = "会员";

    @Column(name = "code")
    private String code; // 用户在当前店铺开通的账户的编号

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "title")
    private String title; // 会员等级

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "balance")
    private BigDecimal balance; // 余额

    @Column(name = "password")
    private String password; // 顾客在当前商铺的付款密码

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public ShopAccount() {
    }

    public ShopAccount(String name, Gender gender, String title, String phoneNumber, BigDecimal balance, Shop shop) {
        this.name = name;
        this.gender = gender;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.shop = shop;
    }

    @PrePersist
    public void init() {
        this.code = RandomStringUtils.randomAlphanumeric(SHOP_ACCOUNT_CODE_LENGTH).toLowerCase();
    }
}
