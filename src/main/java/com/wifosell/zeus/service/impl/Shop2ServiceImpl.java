package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.shop.AddShopRequest;
import com.wifosell.zeus.payload.request.shop.IShopRequest;
import com.wifosell.zeus.payload.request.shop.UpdateShopRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaSwwAndEcomAccountRepository;
import com.wifosell.zeus.service.Shop2Service;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.specs.ShopSpecs;
import com.wifosell.zeus.specs.WarehouseSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    private final SaleChannelShopRepository saleChannelShopRepository;
    private final WarehouseRepository warehouseRepository;
    private final EcomAccountRepository ecomAccountRepository;

    private final LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;

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
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Shop shop = new Shop();
        return this.updateShopByRequest(gm, shop, request);
    }

    @Override
    public Shop updateShop(Long userId, Long shopId, UpdateShopRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Shop shop = this.getShop(userId, shopId);
        return this.updateShopByRequest(gm, shop, request);
    }

    @Override
    public Shop activateShop(Long userId, Long shopId) {
        Shop shop = this.getShop(userId, shopId);
        shop.setIsActive(true);
        return shopRepository.save(shop);
    }

    @Override
    public Shop deactivateShop(Long userId, Long shopId) {
        Shop shop = this.getShop(userId, shopId);
        shop.setIsActive(false);
        return shopRepository.save(shop);
    }

    @Override
    public List<Shop> activateShops(Long userId, List<Long> shopIds) {
        return shopIds.stream().map(id -> this.activateShop(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Shop> deactivateShops(Long userId, List<Long> shopIds) {
        return shopIds.stream().map(id -> this.deactivateShop(userId, id)).collect(Collectors.toList());
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

        // Link with Sale Channels & Warehouses
        Optional.ofNullable(request.getRelations()).ifPresent(requestRelations -> {
            List<Long> requestSaleChannelIds = requestRelations.stream()
                    .map(IShopRequest.Relation::getSaleChannelId)
                    .collect(Collectors.toList());



            List<Long> requestEcomIds = requestRelations.stream()
                    .map(IShopRequest.Relation::getEcomId)
                    .collect(Collectors.toList());


            List<Long> curSaleChannelIds = shop.getSaleChannelShops().stream()
                    .map(SaleChannelShop::getSaleChannel)
                    .map(SaleChannel::getId)
                    .collect(Collectors.toList());



            // Remove relations
            curSaleChannelIds.forEach(curSaleChannelId -> {
                if (!requestSaleChannelIds.contains(curSaleChannelId)) {
                    saleChannelShopRepository.deleteByShopIdAndSaleChannelId(shop.getId(), curSaleChannelId);

                    Optional<SaleChannelShop> relationOptional = shop.getSaleChannelShops().stream()
                            .filter(relation -> relation.getSaleChannel().getId().equals(curSaleChannelId))
                            .findFirst();
                    relationOptional.ifPresent(relation -> shop.getSaleChannelShops().remove(relation));

                }
            });

            // Add new relations
            requestRelations.forEach(requestRelation -> {
                Warehouse warehouse = warehouseRepository.getOne(
                        WarehouseSpecs.hasGeneralManager(gm.getId())
                                .and(WarehouseSpecs.hasId(requestRelation.getWarehouseId()))
                );
                SaleChannelShop relation;
                if (curSaleChannelIds.contains(requestRelation.getSaleChannelId())) {
                    relation = saleChannelShopRepository.getByShopIdAndSaleChannelId(
                            shop.getId(), requestRelation.getSaleChannelId());
                    if (relation != null) {
                        relation.setWarehouse(warehouse);
                        saleChannelShopRepository.save(relation);
                    }

                } else {
                    SaleChannel saleChannel = saleChannelRepository.getOne(
                            SaleChannelSpecs.hasGeneralManager(gm.getId())
                                    .and(SaleChannelSpecs.hasId(requestRelation.getSaleChannelId()))
                    );
                    relation = SaleChannelShop.builder()
                            .shop(shop)
                            .saleChannel(saleChannel)
                            .warehouse(warehouse)
                            .build();
                    saleChannelShopRepository.save(relation);
                    shop.getSaleChannelShops().add(relation);
                }

                //SaleChannelShop with ecom account

                if(requestRelation.getEcomId() != null) {
                    Optional<EcomAccount> ecomAccount = ecomAccountRepository.findById(requestRelation.getEcomId());

                    if (ecomAccount.isPresent()) {

                        Optional<LazadaSwwAndEcomAccount> relationSwwEAOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(requestRelation.getEcomId());
                        LazadaSwwAndEcomAccount linkSwwEaRelation;

                        if (relationSwwEAOpt.isEmpty()) {
                            linkSwwEaRelation = LazadaSwwAndEcomAccount.builder()
                                    .ecomAccount(ecomAccount.get())
                                    .saleChannelShop(relation).build();
                        } else {
                            linkSwwEaRelation = relationSwwEAOpt.get();
                            linkSwwEaRelation.setSaleChannelShop(relation);
                        }
                        lazadaSwwAndEcomAccountRepository.save(linkSwwEaRelation);
                        if(relation.getLazadaSwwAndEcomAccount() ==null) {
                            relation.setLazadaSwwAndEcomAccount(new ArrayList<>());
                        }
                        relation.getLazadaSwwAndEcomAccount().add(linkSwwEaRelation);
                    }
                }

            });
        });
        shopRepository.save(shop);
        Shop fetchShop = getShop(gm.getId(), shop.getId());
        return fetchShop;
    }
}
