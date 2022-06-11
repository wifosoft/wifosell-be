package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.shop.AddShopRequest;
import com.wifosell.zeus.payload.request.shop.IShopRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.specs.SaleChannelSpecs;
import com.wifosell.zeus.specs.WarehouseSpecs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopSeeder extends BaseSeeder implements ISeeder {
    private UserRepository userRepository;
    private ShopRepository shopRepository;
    private SaleChannelRepository saleChannelRepository;
    private SaleChannelShopRepository saleChannelShopRepository;
    private WarehouseRepository warehouseRepository;

    @Override
    public void prepareJpaRepository() {
        userRepository = factory.getRepository(UserRepository.class);
        shopRepository = factory.getRepository(ShopRepository.class);
        saleChannelRepository = factory.getRepository(SaleChannelRepository.class);
        saleChannelShopRepository = factory.getRepository(SaleChannelShopRepository.class);
        warehouseRepository = factory.getRepository(WarehouseRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/com/wifosell/zeus/database/data/shop.json");

        try {
            AddShopRequest[] requests = mapper.readValue(file, AddShopRequest[].class);
            for (AddShopRequest request : requests) {
                this.addShopByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addShopByRequest(AddShopRequest request, User gm) {
        Shop shop = new Shop();

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
                if (!curSaleChannelIds.contains(requestRelation.getSaleChannelId())) {
                    SaleChannel saleChannel = saleChannelRepository.getOne(
                            SaleChannelSpecs.hasGeneralManager(gm.getId())
                                    .and(SaleChannelSpecs.hasId(requestRelation.getSaleChannelId()))
                    );
                    Warehouse warehouse = warehouseRepository.getOne(
                            WarehouseSpecs.hasGeneralManager(gm.getId())
                                    .and(WarehouseSpecs.hasId(requestRelation.getWarehouseId()))
                    );

                    SaleChannelShop relation = SaleChannelShop.builder()
                            .shop(shop)
                            .saleChannel(saleChannel)
                            .warehouse(warehouse)
                            .build();
                    saleChannelShopRepository.save(relation);
                    shop.getSaleChannelShops().add(relation);
                }
            });
        });

        shopRepository.save(shop);
    }
}
