package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    List<Shop> getCreatedShop(Long userId);

    List<Shop> getRelevantShop(Long userId);

    List<Shop> getCanAccessShop(Long userId);

    void givePermissionManageShop(Long userId, Long shopId);
}
