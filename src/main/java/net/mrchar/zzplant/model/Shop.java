package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`shop`")
public class Shop extends AbstractPersistable<UUID> {
    private static final Integer SHOP_CODE_LENGTH = 10;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "shop", fetch = FetchType.EAGER)
    private Set<ShopAssistant> assistants;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    public Shop() {
    }

    public Shop(String name, String address, User owner) {
        this.name = name;
        this.address = address;
        this.owner = owner;
    }

    @PrePersist
    public void init() {
        this.code = RandomStringUtils.randomAlphanumeric(SHOP_CODE_LENGTH).toLowerCase();
    }
}
