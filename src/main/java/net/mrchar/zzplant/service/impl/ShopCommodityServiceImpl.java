package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ResourceAlreadyExistsException;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.ShopCommodity;
import net.mrchar.zzplant.repository.ShopCommodityRepository;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.service.ShopCommodityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ShopCommodityServiceImpl implements ShopCommodityService {
    private final ShopRepository shopRepository;
    private final ShopCommodityRepository shopCommodityRepository;

    @Override
    @Transactional
    public ShopCommodity addShopCommodity(String shopCode, String name, BigDecimal price) {
        Shop shop = this.shopRepository.findOneByCode(shopCode)
                .orElseThrow(() -> new ResourceNotExistsException("店铺不存在"));

        this.shopCommodityRepository.findOneByShopAndName(shop, name)
                .ifPresent((commodity) -> {
                    throw new ResourceAlreadyExistsException("商品名称不能重复");
                });

        ShopCommodity shopCommodity = new ShopCommodity(name, price, false, shop);
        return this.shopCommodityRepository.save(shopCommodity);
    }

    @Override
    @Transactional
    public ShopCommodity updateShopCommodity(String shopCode, String commodityCode, UpdateShopCommodityOption option) {
        ShopCommodity entity = this.shopCommodityRepository.findOneByShopCodeAndCode(shopCode, commodityCode)
                .orElseThrow(() -> new ResourceNotExistsException("商品不存在"));

        if (option.getName() != null) {
            entity.setName(option.getName());
        }

        if (option.getPrice() != null) {
            entity.setPrice(option.getPrice());
        }

        if (option.getOffShelf() != null) {
            entity.setOffShelf(option.getOffShelf());
        }

        this.shopCommodityRepository.save(entity);
        return entity;
    }

    @Override
    @Transactional
    public ShopCommodity deleteShopCommodity(String shopCode, String commodityCode) {
        ShopCommodity entity = this.shopCommodityRepository.findOneByShopCodeAndCode(shopCode, commodityCode)
                .orElseThrow(() -> new ResourceNotExistsException("商品不存在"));

        this.shopCommodityRepository.deleteById(entity.getId());
        return entity;
    }
}
