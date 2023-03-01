package net.mrchar.zzplant.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Getter
@Entity
@Table(name = "`account`")
public class Account extends AbstractPersistable<UUID> {
    @Column(name = "code")
    private String code; // 账户编码，系统外部索引
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
}