package net.mrchar.zzplant.repository;

import net.mrchar.zzplant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * 根据账户名称查找账户属于的用户
     *
     * @param accountName 账户名
     * @return 用户信息
     */
    @Query("select user from User user where user.account.name = :accountName")
    Optional<User> findOneByAccountName(String accountName);
}