package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.shop.AddShopRequest;
import com.wifosell.zeus.payload.request.shop.IShopRequest;
import com.wifosell.zeus.payload.request.shop.UpdateShopRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.SaleChannelShopRelationRepository;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.Shop2Service;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.specs.ShopSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("Shop2Service")
@RequiredArgsConstructor
public class Shop2ServiceImpl implements Shop2Service {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final SaleChannelRepository saleChannelRepository;
    private final SaleChannelShopRelationRepository saleChannelShopRelationRepository;

    @Override
    public Page<Shop> getShops(Long userId, List<Boolean> isActives, Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return shopRepository.findAll(
                ShopSpecs.hasGeneralManager(gmId)
                        .and(ShopSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Shop getShop(Long userId, Long shopId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return shopRepository.getOne(
                ShopSpecs.hasGeneralManager(gmId)
                        .and(ShopSpecs.hasId(shopId))
        );
    }

    @Override
    public Shop addShop(Long userId, AddShopRequest request) {
        return null;
    }

    @Override
    public Shop updateShop(Long userId, Long shopId, UpdateShopRequest request) {
        return null;
    }

    @Override
    public Shop activateShop(Long userId, Long shopId) {
        return null;
    }

    @Override
    public Shop deactivateShop(Long userId, Long shopId) {
        return null;
    }

    @Override
    public List<Shop> activateShops(Long userId, List<Long> shopIds) {
        return null;
    }

    @Override
    public List<Shop> deactivateShops(Long userId, List<Long> shopIds) {
        return null;
    }

    private Shop updateShopByRequest(User gm, Shop shop, IShopRequest request) {
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
