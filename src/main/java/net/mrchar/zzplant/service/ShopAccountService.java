package net.mrchar.zzplant.service;

import java.math.BigDecimal;

public interface ShopAccountService {
    /**
     * 给指定账户充值
     *
     * @param shopCode    商铺编号
     * @param accountCode 会员编号
     * @param amount      充值金额
     */
    void topUp(String shopCode, String accountCode, BigDecimal amount);
}
