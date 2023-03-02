package net.mrchar.zzplant.service.impl;

import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.model.Shop;
import net.mrchar.zzplant.model.ShopAssistant;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.ShopAssistantRepository;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.service.ShopService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private static final Integer SHOP_CODE_LENGTH = 10;

    private final ShopRepository shopRepository;
    private final ShopAssistantRepository assistantRepository;

    @Override
    @Transactional
    public Shop addShop(String name, String address, User owner) {
        String code = RandomStringUtils.randomAlphanumeric(SHOP_CODE_LENGTH).toLowerCase();
        Shop shop = new Shop(code, name, address, owner);
        this.shopRepository.save(shop);

        ShopAssistant assistant = new ShopAssistant(String.format("%04d", 1),
                owner.getName(), "经理", owner, shop);
        this.assistantRepository.save(assistant);

        shop.setAssistants(Set.of(assistant));
        return shop;
    }
}
