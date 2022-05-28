package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public interface ShopService {
    List<Shop> getCreatedShop(Long userId);

    List<Shop> getRelevantShop(Long userId);

    List<Shop> getCanAccessShop(Long userId);

    void givePermissionManageShop(Long userId, Long shopId);

    //API start
    Shop getShopInfo(Long shopId);

    Shop addShop(@NonNull Long userId, @Valid ShopRequest request);

    Shop updateShop(@NonNull Long userId, @NonNull Long shopId, @Valid ShopRequest request);

    Shop deActivateShop(Long shopId);

    Shop activateShop(Long shopId);

    List<Shop> activateShops(List<Long> shopIds);

    List<Shop> deactivateShops(List<Long> shopIds);

    List<User> getListStaffOfShop(Long shopId);
}
