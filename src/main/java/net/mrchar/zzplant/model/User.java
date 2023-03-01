package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;


@Getter
@Entity
@Table(name = "`user`")
public class User extends AbstractPersistable<UUID> {
    @Column(name = "name")
    private String name;

    @Enumerated(STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
}