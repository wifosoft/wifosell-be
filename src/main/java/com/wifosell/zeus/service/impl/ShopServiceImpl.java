package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service("ShopService")
public class ShopServiceImpl implements ShopService {
    Logger logger = Logger.getLogger(ShopServiceImpl.class.getName());

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    UserRepository userRepository;


    @PersistenceContext
    private EntityManager em;




    @Override
    public List<Shop> getManagedShop(Long userId) {
        User currentUser = userRepository.getById(userId);
        return currentUser.getShops();
    }

    /*
         Lấy toàn bộ shop liên quan
         */
    @Override
    public List<Shop> getRelevantShop(Long userId) {
        User currentUser = userRepository.getById(userId);
        if (currentUser.isRoot()) {
            //nếu user là tài khoản root thì lấy luôn
            return currentUser.getShops();
        } else {
            //Ngược lại lấy tài khoản parent
            User parentUser = userRepository.getUserByName(currentUser.getParent().getUsername());
            return parentUser.getShops();
        }
    }


    /*
     * Kiêm tra tài khoản có nằm trong cùng hệ thống cửa hàng hay không.
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

        List<Shop> currentShopOfUser = userNeedToAdd.getShops();


        boolean existed = shopRepository.existsUserMangageShop(userId, shopId);
        if (existed) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SHOP_MANAGED_BY_THIS_USER));
        }
        currentShopOfUser.add( shopNeedToAssign );
        userNeedToAdd.setShops(currentShopOfUser);
        userRepository.save(userNeedToAdd);
    }

    /**
     * API lấy thông tin cửa hàng
     * @param shopId
     * @return
     */
    @Override
    public Shop getShopInfo(Long shopId) {
        return shopRepository.getShopById(shopId);
    }

    /**
     * API thêm cửa hàng mới
     * @param shopRequest
     * @return
     */
    @Override
    public Shop addShop(ShopRequest shopRequest, Long userId) {
        User parentUser = userRepository.getUserById(userId);
        Shop shop = new Shop();
        Optional.ofNullable(shopRequest.getName()).ifPresent(shop::setName);
        Optional.ofNullable(shopRequest.getShortName()).ifPresent(shop::setShortName);
        Optional.ofNullable(shopRequest.getAddress()).ifPresent(shop::setAddress);
        Optional.ofNullable(shopRequest.getPhone()).ifPresent(shop::setPhone);
        Optional.ofNullable(shopRequest.getDescription()).ifPresent(shop::setDescription);
        Optional.ofNullable(shopRequest.getBusinessLine()).ifPresent(shop::setBusinessLine);
        shop.setGeneralManager(parentUser);
        shop  = shopRepository.save(shop);
        return shop;
    }
}
