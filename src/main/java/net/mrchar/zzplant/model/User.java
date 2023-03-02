package net.mrchar.zzplant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;


@Getter
@Entity
@Table(name = "`user`")
public class User extends AbstractPersistable<UUID> {
    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Enumerated(STRING)
    @Column(name = "gender")
    private Gender gender;

    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public User() {
    }

    public User(String name, Gender gender, String phoneNumber, Account account) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.account = account;
    }
}