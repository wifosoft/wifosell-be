package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.UserShopRelationRepository;
import com.wifosell.zeus.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
@Transactional
@Service("ShopService")
public class ShopServiceImpl implements ShopService {
    Logger logger = Logger.getLogger(ShopServiceImpl.class.getName());

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserShopRelationRepository userShopRelationRepository;
    @PersistenceContext
    private EntityManager em;


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

    /**
     * API lấy thông tin cửa hàng
     *
     * @param shopId
     * @return
     */
    @Override
    public Shop getShopInfo(Long shopId) {
        return shopRepository.getShopById(shopId);
    }

    /**
     * API thêm cửa hàng mới
     *
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
        shop = shopRepository.save(shop);
        return shop;
    }

    @Override
    public Shop editShop(Long shopId, ShopRequest shopRequest) {
        Shop shop = shopRepository.getShopById(shopId);
        Optional.ofNullable(shopRequest.getName()).ifPresent(shop::setName);
        Optional.ofNullable(shopRequest.getShortName()).ifPresent(shop::setShortName);
        Optional.ofNullable(shopRequest.getAddress()).ifPresent(shop::setAddress);
        Optional.ofNullable(shopRequest.getPhone()).ifPresent(shop::setPhone);
        Optional.ofNullable(shopRequest.getDescription()).ifPresent(shop::setDescription);
        Optional.ofNullable(shopRequest.getBusinessLine()).ifPresent(shop::setBusinessLine);
        shop = shopRepository.save(shop);
        return shop;
    }

    @Override
    public Shop deActiveShop(Long shopId) {
        Shop shop = shopRepository.getShopById(shopId);
        shop.setIsActive(false);
        return shopRepository.save(shop);
    }

    @Override
    public Shop activeShop(Long shopId) {
        Shop shop = shopRepository.getShopById(shopId);
        shop.setIsActive(true);
        return shopRepository.save(shop);
    }

    /**
     * Lấy danh sách nhân viên có quyền truy cập vào có có id: shopId
     * @param shopId
     * @return
     */
    
    @Override
    public List<User> getListStaffOfShop(Long shopId) {
        Shop shop  = shopRepository.getShopById(shopId);
        Set<UserShopRelation> userRelation = shop.getUserShopRelation();
        List<User> users = userRelation.stream().map(e -> e.getUser() ).collect(Collectors.toList());
        return users;
    }
}
