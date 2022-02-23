package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.shop.WarehouseShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.repository.*;
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

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final UserShopRelationRepository userShopRelationRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseShopRelationRepository warehouseShopRelationRepository;
    private final SaleChannelRepository saleChannelRepository;
    private final SaleChannelShopRelationRepository saleChannelShopRelationRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository,
                           UserRepository userRepository,
                           UserShopRelationRepository userShopRelationRepository,
                           WarehouseRepository warehouseRepository,
                           WarehouseShopRelationRepository warehouseShopRelationRepository,
                           SaleChannelRepository saleChannelRepository,
                           SaleChannelShopRelationRepository saleChannelShopRelationRepository) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.userShopRelationRepository = userShopRelationRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseShopRelationRepository = warehouseShopRelationRepository;
        this.saleChannelRepository = saleChannelRepository;
        this.saleChannelShopRelationRepository = saleChannelShopRelationRepository;
    }

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
        this.updateShopByRequest(shop, shopRequest);
        shop.setGeneralManager(parentUser);
        shop = shopRepository.save(shop);
        return shop;
    }

    @Override
    public Shop editShop(Long shopId, ShopRequest shopRequest) {
        Shop shop = shopRepository.getShopById(shopId);
        this.updateShopByRequest(shop, shopRequest);
        shop = shopRepository.save(shop);
        return shop;
    }

    @Override
    public Shop deActivateShop(Long shopId) {
        Shop shop = shopRepository.getShopById(shopId);
        shop.setIsActive(false);
        return shopRepository.save(shop);
    }

    @Override
    public Shop activateShop(Long shopId) {
        Shop shop = shopRepository.getShopById(shopId);
        shop.setIsActive(true);
        return shopRepository.save(shop);
    }

    @Override
    public List<Shop> activateShops(List<Long> shopIds) {
        return shopIds.stream().map(this::activateShop).collect(Collectors.toList());
    }

    @Override
    public List<Shop> deactivateShops(List<Long> shopIds) {
        return shopIds.stream().map(this::deActivateShop).collect(Collectors.toList());
    }

    @Override
    public void linkWarehouseToShop(Long warehouseId, Long shopId) {

        if (warehouseShopRelationRepository.existsWarehouseShopRelationByShopAndWarehouse(shopId, warehouseId)) {
            //existed
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.RECORD_EXISTED));
        }

        warehouseShopRelationRepository.save(
                WarehouseShopRelation.builder().shop(
                        shopRepository.getShopById(shopId)
                ).warehouse(
                        warehouseRepository.getWarehouseById(warehouseId)
                ).build()
        );

    }

    @Override
    public void linkWarehouseToShop(Long currentUserId, Long warehouseId, Long shopId) {
        Warehouse warehouse = warehouseRepository.getWarehouseById(warehouseId);
        Shop shop = shopRepository.getShopById(shopId);

        if (!warehouse.getGeneralManager().getId().equals(currentUserId) || !shop.getGeneralManager().getId().equals(currentUserId)) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PERMISSION_DENIED));
        }
        if (warehouseShopRelationRepository.existsWarehouseShopRelationByShopAndWarehouse(shopId, warehouseId)) {
            //existed
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.RECORD_EXISTED));
        }

        warehouseShopRelationRepository.save(
                WarehouseShopRelation.builder().shop(shop).warehouse(warehouse).build()
        );
    }

    @Override
    public void linkSaleChannelToShop(Long currentUserId, Long saleChannelId, Long shopId) {
        SaleChannel saleChannel = saleChannelRepository.findSaleChannelById(saleChannelId);
        Shop shop = shopRepository.getShopById(shopId);

        if (!saleChannel.getGeneralManager().getId().equals(currentUserId) || !shop.getGeneralManager().getId().equals(currentUserId)) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PERMISSION_DENIED));
        }

        if (saleChannelShopRelationRepository.existsSaleChannelShopRelationByShopAndSaleChannel(shopId, saleChannelId)) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.RECORD_EXISTED));
        }

        saleChannelShopRelationRepository.save(
                SaleChannelShopRelation.builder().shop(shop).saleChannel(saleChannel).build()
        );
    }

    /**
     * Lấy danh sách nhân viên có quyền truy cập vào có có id: shopId
     *
     * @param shopId
     * @return
     */

    @Override
    public List<User> getListStaffOfShop(Long shopId) {
        Shop shop = shopRepository.getShopById(shopId);
        Set<UserShopRelation> userRelation = shop.getUserShopRelation();
        return userRelation.stream().map(UserShopRelation::getUser).collect(Collectors.toList());
    }

    private void updateShopByRequest(Shop shop, ShopRequest shopRequest) {
        Optional.ofNullable(shopRequest.getName()).ifPresent(shop::setName);
        Optional.ofNullable(shopRequest.getShortName()).ifPresent(shop::setShortName);
        Optional.ofNullable(shopRequest.getAddress()).ifPresent(shop::setAddress);
        Optional.ofNullable(shopRequest.getPhone()).ifPresent(shop::setPhone);
        Optional.ofNullable(shopRequest.getDescription()).ifPresent(shop::setDescription);
        Optional.ofNullable(shopRequest.getBusinessLine()).ifPresent(shop::setBusinessLine);
        Optional.ofNullable(shopRequest.getActive()).ifPresent(shop::setIsActive);
    }
}
