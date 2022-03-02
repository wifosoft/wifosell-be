package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    List<Shop> getCreatedShop(Long userId);
    List<Shop> getRelevantShop(Long userId);
    List<Shop> getCanAccessShop(Long userId);

    void givePermissionManageShop(Long userId,  Long shopId);

    //API start
    Shop getShopInfo(Long shopId);

    Shop addShop(ShopRequest shopRequest, Long userId);
    Shop editShop(Long shopId ,ShopRequest shopRequest);
    Shop deActivateShop(Long shopId);
    Shop activateShop(Long shopId);
    List<Shop> activateShops(List<Long> shopIds);
    List<Shop> deactivateShops(List<Long> shopIds);

    void linkWarehouseToShop(Long warehouseId,  Long shopId);
    void linkWarehouseToShop(Long currentUserId, Long warehouseId,  Long shopId);

    void linkSaleChannelToShop(Long currentUserId, Long saleChannelId, Long shopId);

    List<User> getListStaffOfShop(Long shopId);


}
