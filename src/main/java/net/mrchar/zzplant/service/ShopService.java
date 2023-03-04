package net.mrchar.zzplant.service;

import net.mrchar.zzplant.model.*;

import java.util.Map;

public interface ShopService {
    /**
     * 创建商铺
     *
     * @param name    商铺名称
     * @param address 商铺地址
     * @param owner   商铺所有者
     * @return 商铺信息
     */
    Shop addShop(String name, String address, User owner);

    /**
     * 为商铺添加店员
     *
     * @param shopCode    商铺编号
     * @param username    店员名称
     * @param phoneNumber 店员手机号码
     * @return
     */
    ShopAssistant addShopAssistant(String shopCode, String username, String phoneNumber);

    /**
     * 在商铺中添加会员
     *
     * @param shopCode    商铺编码
     * @param name        会员名称
     * @param gender      会员性别
     * @param phoneNumber 手机号码
     * @return 会员信息
     */
    ShopAccount addShopAccount(String shopCode, String name, Gender gender, String phoneNumber);

    /**
     * 创建账单
     *
     * @param shopCode       商铺编号
     * @param accountCode    会员编号
     * @param commodityCodes 商品编号列表
     * @return 账单信息
     */
    ShopInvoice addInvoice(String shopCode, String accountCode, Map<String, Integer> commodityCodes);
}
