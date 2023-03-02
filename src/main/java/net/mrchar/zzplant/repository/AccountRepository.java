package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    /**
     * 根据用户的手机号码查找账户
     *
     * @param phoneNumber 用户的手机号码
     * @return 账户信息
     */
    @Query("select account from Account account where account.user.phoneNumber = :phoneNumber")
    Optional<Account> findOneByPhoneNumber(String phoneNumber);

    /**
     * 根据账户名称查找账户
     *
     * @param accountName 账户名称
     * @return 账户信息
     */
    @Query("select account from Account account where account.name = :accountName")
    Optional<Account> findOneByName(String accountName);
}