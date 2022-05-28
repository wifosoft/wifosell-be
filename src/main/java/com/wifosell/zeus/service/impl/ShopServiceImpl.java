package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.ShopService;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    /**
     * API lấy thông tin cửa hàng
     *
     * @param shopId
     * @return
     */
    @Override
    public Shop getShopInfo(Long shopId) {
        return shopRepository.getById(shopId);
    }

    @Override
    public Shop addShop(@NonNull Long userId, @Valid ShopRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Shop shop = new Shop();
        return this.updateShopByRequest(shop, request, gm);
    }

    @Override
    public Shop updateShop(@NonNull Long userId, @NonNull Long shopId, @Valid ShopRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Shop shop = shopRepository.getById(shopId);
        return this.updateShopByRequest(shop, request, gm);
    }

    @Override
    public Shop deActivateShop(Long shopId) {
        Shop shop = shopRepository.getById(shopId);
        shop.setIsActive(false);
        return shopRepository.save(shop);
    }

    @Override
    public Shop activateShop(Long shopId) {
        Shop shop = shopRepository.getById(shopId);
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

    /**
     * Lấy danh sách nhân viên có quyền truy cập vào có có id: shopId
     *
     * @param shopId
     * @return
     */

    @Override
    public List<User> getListStaffOfShop(Long shopId) {
        Shop shop = shopRepository.getById(shopId);
        List<UserShopRelation> userRelation = shop.getUserShopRelations();
        return userRelation.stream().map(UserShopRelation::getUser).collect(Collectors.toList());
    }

    private Shop updateShopByRequest(Shop shop, ShopRequest request, User gm) {
        // Create or update Shop
        Optional.ofNullable(request.getName()).ifPresent(shop::setName);
        Optional.ofNullable(request.getShortName()).ifPresent(shop::setShortName);
        Optional.ofNullable(request.getAddress()).ifPresent(shop::setAddress);
        Optional.ofNullable(request.getPhone()).ifPresent(shop::setPhone);
        Optional.ofNullable(request.getDescription()).ifPresent(shop::setDescription);
        Optional.ofNullable(request.getBusinessLine()).ifPresent(shop::setBusinessLine);
        Optional.ofNullable(request.getIsActive()).ifPresent(shop::setIsActive);
        shop.setGeneralManager(gm);
        shopRepository.save(shop);

        // Link Sale Channels with Shop
        Optional.ofNullable(request.getSaleChannelIds()).ifPresent(requestSaleChannelIds -> {
            List<Long> curSaleChannelIds = shop.getSaleChannelShopRelations().stream()
                    .map(SaleChannelShopRelation::getSaleChannel)
                    .map(SaleChannel::getId)
                    .collect(Collectors.toList());

            // Remove relations
            curSaleChannelIds.forEach(curSaleChannelId -> {
                if (!requestSaleChannelIds.contains(curSaleChannelId)) {
                    saleChannelShopRelationRepository.deleteByShopIdAndSaleChannelId(shop.getId(), curSaleChannelId);
                    Optional<SaleChannelShopRelation> relationOptional = shop.getSaleChannelShopRelations().stream()
                            .filter(relation -> relation.getSaleChannel().getId().equals(curSaleChannelId))
                            .findFirst();
                    relationOptional.ifPresent(relation -> shop.getSaleChannelShopRelations().remove(relation));
                }
            });

            // Add new relations
            requestSaleChannelIds.forEach(requestSaleChannelId -> {
                if (!curSaleChannelIds.contains(requestSaleChannelId)) {
                    SaleChannel saleChannel = saleChannelRepository.getOne(
                            SaleChannelSpecs.hasGeneralManager(gm.getId())
                                    .and(SaleChannelSpecs.hasId(requestSaleChannelId))
                    );
                    SaleChannelShopRelation relation = SaleChannelShopRelation.builder().shop(shop).saleChannel(saleChannel).build();
                    saleChannelShopRelationRepository.save(relation);
                    shop.getSaleChannelShopRelations().add(relation);
                }
            });
        });

        return shopRepository.save(shop);
    }
}
