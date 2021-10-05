package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    List<Shop> getManagedShop(Long userId);
    List<Shop> getRelevantShop(Long userId);

    void givePermissionManageShop(Long userId,  Long shopId);

    //API start
    Shop getShopInfo(Long shopId);
}
