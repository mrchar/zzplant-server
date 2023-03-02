package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("select account from Account account where account.user.phoneNumber = :phoneNumber")
    Optional<Account> findOneByPhoneNumber(String phoneNumber);
}