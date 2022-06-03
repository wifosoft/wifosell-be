package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.request.shop.AddShopRequest;
import com.wifosell.zeus.payload.request.shop.UpdateShopRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface Shop2Service {
    Page<Shop> getShops(Long userId, List<Boolean> isActives,
                        Integer offset, Integer limit, String sortBy, String orderBy);

    Shop getShop(Long userId, Long shopId);

    Shop addShop(Long userId, AddShopRequest request);

    Shop updateShop(Long userId, Long shopId, UpdateShopRequest request);

    Shop activateShop(Long userId, Long shopId);

    Shop deactivateShop(Long userId, Long shopId);

    List<Shop> activateShops(Long userId, List<Long> shopIds);

    List<Shop> deactivateShops(Long userId, List<Long> shopIds);
}
