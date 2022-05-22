package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.repository.SaleChannelRepository;
import com.wifosell.zeus.repository.SaleChannelShopRelationRepository;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.specs.SaleChannelSpecs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShopSeeder extends BaseSeeder implements ISeeder {
    private UserRepository userRepository;
    private ShopRepository shopRepository;
    private SaleChannelRepository saleChannelRepository;
    private SaleChannelShopRelationRepository saleChannelShopRelationRepository;

    @Override
    public void prepareJpaRepository() {
        userRepository = factory.getRepository(UserRepository.class);
        shopRepository = factory.getRepository(ShopRepository.class);
        saleChannelRepository = factory.getRepository(SaleChannelRepository.class);
        saleChannelShopRelationRepository = factory.getRepository(SaleChannelShopRelationRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/com/wifosell/zeus/database/data/shop.json");

        try {
            ShopRequest[] requests = mapper.readValue(file, ShopRequest[].class);
            for (ShopRequest request : requests) {
                this.addShopByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addShopByRequest(ShopRequest request, User gm) {
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

        shopRepository.save(shop);
    }
}
