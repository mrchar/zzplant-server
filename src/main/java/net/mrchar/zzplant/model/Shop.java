package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "`shop`")
public class Shop extends AbstractPersistable<UUID> {
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
}
