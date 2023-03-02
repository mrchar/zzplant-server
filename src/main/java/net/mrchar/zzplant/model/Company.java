package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`company`")
public class Company extends AbstractPersistable<UUID> {
    @Column(name = "code")
    private String code; // 公司代码，通常使用统一社会信用代码
    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User user;
}
