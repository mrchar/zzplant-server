package net.mrchar.zzplant.service;

import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.User;

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
}
