package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.security.UserPrincipal;
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
    Shop deActiveShop(Long shopId);
    Shop activeShop(Long shopId);

    List<User> getListStaffOfShop(Long shopId);


}
