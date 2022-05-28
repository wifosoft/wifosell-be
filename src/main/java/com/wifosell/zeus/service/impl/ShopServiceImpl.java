package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Transactional
@Service("ShopService")
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    Logger logger = Logger.getLogger(ShopServiceImpl.class.getName());

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final UserShopRelationRepository userShopRelationRepository;
    private final SaleChannelRepository saleChannelRepository;
    private final SaleChannelShopRelationRepository saleChannelShopRelationRepository;

    @Override
    public List<Shop> getCreatedShop(Long userId) {
        User currentUser = userRepository.getById(userId);
        return currentUser.getListCreatedShops();
    }


    /**
     * Lấy toàn bộ shop cha tạo ra
     */
    @Override
    public List<Shop> getRelevantShop(Long userId) {
        User currentUser = userRepository.getById(userId);
        if (currentUser.isRoot()) {
            //nếu user là tài khoản root thì lấy luôn
            return currentUser.getListCreatedShops();
        } else {
            //Ngược lại lấy tài khoản parent
            User parentUser = userRepository.getUserByName(currentUser.getParent().getUsername());
            return parentUser.getListCreatedShops();
        }
    }

    /**
     * Danh sách shop quản lý bởi userId
     *
     * @param userId
     * @return
     */
    @Override
    public List<Shop> getCanAccessShop(Long userId) {
        User currentUser = userRepository.getById(userId);
        return currentUser.getAccessShops();
    }


    /**
     * Kiểm cha user có thể assign vào shop
     *
     * @param userId
     * @param shopId
     * @return
     */
    public boolean checkUserCanAssignToShop(Long userId, Long shopId) {
        List<Shop> shops = this.getRelevantShop(userId);

        for (Shop shop : shops) {
            if (shop.getId().equals(shopId)) {
                return true;
            }
        }
        return false;
    }


    @Transactional
    @Override
    public void givePermissionManageShop(Long userId, Long shopId) {
        User userNeedToAdd = userRepository.findById(userId).
                orElseThrow(() -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND, "User not found")));
        Shop shopNeedToAssign = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SHOP_NOT_FOUND)));

        // Kiểm tra user có thể được assign vào shop này không
        if (!checkUserCanAssignToShop(userId, shopId)) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_IN_RELEVANT_SHOP));
        }

        boolean existed = userShopRelationRepository.existsUserShopRelationsByUserIsAndShopIs(userId, shopId);

        if (existed) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SHOP_MANAGED_BY_THIS_USER));
        }
        UserShopRelation userShopRelation = new UserShopRelation();
        userShopRelation.setUser(userNeedToAdd);
        userShopRelation.setShop(shopNeedToAssign);
        userShopRelationRepository.save(userShopRelation);
    }
}
