package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Getter
@Entity
@Table(name = "`shop_assistant`")
public class ShopAssistant extends AbstractPersistable<UUID> {
    @Column(name = "code")
    private String code; // 工号
    @Column(name = "name")
    private String name; // 在店铺中的昵称，默认使用User的名称
    @Column(name = "title")
    private String title; // 头衔
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
