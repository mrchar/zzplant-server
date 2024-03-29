package net.mrchar.zzplant.repository;

import jakarta.persistence.LockModeType;
import net.mrchar.zzplant.model.ShopAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopAccountRepository extends JpaRepository<ShopAccount, UUID> {
    /**
     * 列出商铺中的所有会员
     *
     * @param shopCode 商铺编号
     * @return 会员列表
     */
    @Query("select account from ShopAccount account where account.shop.code = :shopCode")
    Page<ShopAccount> findAllByShopCode(String shopCode, Pageable pageable);

    /**
     * 使用关键词搜索商铺中的会员
     *
     * @param shopCode
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("select account from ShopAccount account where account.shop.code = :shopCode " +
            "and (account.name like :#{'%'+#keyword+'%'} or account.phoneNumber like :#{'%'+#keyword+'%'})")
    Page<ShopAccount> searchShopAccountOfShop(String shopCode, String keyword, Pageable pageable);

    /**
     * 根据手机号查找指定商铺中的会员
     *
     * @param shopCode    商铺编号
     * @param phoneNumber 会员手机号码
     * @return 会员信息
     */
    @Query("select account from ShopAccount account where account.shop.code = :shopCode and account.phoneNumber = :phoneNumber")
    Optional<ShopAccount> findOneByShopCodeAndPhoneNumber(String shopCode, String phoneNumber);

    /**
     * 判断手机号已经在指定商铺中办理会员
     *
     * @param shopCode    商铺编号
     * @param phoneNumber 手机号码
     * @return 会员是否已经存在
     */
    @Query("select (count(1) > 0) from ShopAccount account where account.shop.code = :shopCode and account.phoneNumber = :phoneNumber")
    boolean existsByShopCodeAndPhoneNumber(String shopCode, String phoneNumber);

    /**
     * 在指定商铺中查找会员
     *
     * @param shopCode    商铺编号
     * @param accountCode 会员编号
     * @return 会员信息
     */
    @Query("select account from ShopAccount account where account.shop.code = :shopCode and account.code = :accountCode")
    Optional<ShopAccount> findOneByShopCodeAndCode(String shopCode, String accountCode);

    /**
     * 获取指定的商铺中的会员，并锁行
     *
     * @param id 商铺会员ID
     * @return 会员信息
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from ShopAccount account where account.id = :id")
    Optional<ShopAccount> findOneByIdForUpdate(UUID id);
}