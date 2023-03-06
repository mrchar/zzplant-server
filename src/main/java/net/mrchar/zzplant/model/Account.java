package net.mrchar.zzplant.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`account`")
public class Account extends AbstractPersistable<UUID> {
    private static final int CODE_LENGTH = 8;

    @Column(name = "code")
    private String code; // 账户编码，系统外部索引

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "account")
    private User user;

    public Account() {
    }

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @PrePersist
    public void init() {
        this.code = RandomStringUtils
                .randomAlphanumeric(CODE_LENGTH)
                .toLowerCase();
    }
}