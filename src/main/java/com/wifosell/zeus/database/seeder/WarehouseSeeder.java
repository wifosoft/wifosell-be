package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.WarehouseShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.WarehouseRepository;
import com.wifosell.zeus.repository.WarehouseShopRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseSeeder  extends BaseSeeder implements ISeeder {
    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    WarehouseShopRelationRepository warehouseShopRelationRepository;

    @Override
    public void prepareJpaRepository() {
        warehouseRepository = factory.getRepository(WarehouseRepository.class);
        userRepository = factory.getRepository(UserRepository.class);
        shopRepository = factory.getRepository(ShopRepository.class);
        warehouseShopRelationRepository = factory.getRepository(WarehouseShopRelationRepository.class);

    }

    @Override
    public void run() {
        User manager1 = userRepository.getUserByName("manager1");
        User manager2 = userRepository.getUserByName("manager2");

        List<Shop> shops = shopRepository.findAll();

        List<Warehouse> listWarehouse = new ArrayList<>();

        listWarehouse.add(Warehouse.builder()
                .name("Kho thứ 1 - QL1CH1")
                .address("Quận 1 Hồ Chí Minh")
                .phone("0982259245")
                .shortName("K1-QL1CH1")
                .description("Kho quần áo quận 1")
                .generalManager(manager1).build());

        listWarehouse.add(Warehouse.builder()
                .name("Kho thứ 2 - QL1CH1")
                .address("Quận 2 Hồ Chí Minh")
                .phone("0982259245")
                .shortName("K2-QL1CH1")
                .description("Kho quần áo quận 2")
                .generalManager(manager1).build());

        listWarehouse.add(Warehouse.builder()
                .name("Kho thứ 3 - QL1CH1")
                .address("Quận 3 Hồ Chí Minh")
                .phone("0982259245")
                .shortName("K3-QL1CH1")
                .description("Kho quần áo quận 3")
                .generalManager(manager1).build());

        listWarehouse.add(Warehouse.builder()
                .name("Kho thứ 1 - QL1CH2")
                .address("Quận 2 Hồ Chí Minh")
                .phone("0982259245")
                .shortName("K2-QL1CH2")
                .description("Kho quần áo quận 2")
                .generalManager(manager1).build());

        listWarehouse.add(Warehouse.builder()
                .name("Kho thứ 1 - QL2CH1")
                .address("Quận 1 Hồ Chí Minh")
                .phone("0982259245")
                .shortName("K1-QL2CH1")
                .description("Kho quần áo quận 1")
                .generalManager(manager2).build());
        listWarehouse.add(Warehouse.builder()
                .name("Kho thứ 1 - QL2CH2")
                .address("Quận 1 Hồ Chí Minh")
                .phone("0982259245")
                .shortName("K1-QL2CH2")
                .description("Kho quần áo quận 1")
                .generalManager(manager2).build());

        warehouseRepository.saveAll(listWarehouse);

        List<WarehouseShopRelation> warehouseShopRelations = new ArrayList<>();
        warehouseShopRelations.add(WarehouseShopRelation.builder().warehouse(listWarehouse.get(0)).shop(shops.get(0)).build()); //warehouse 1 - shop1
        warehouseShopRelations.add(WarehouseShopRelation.builder().warehouse(listWarehouse.get(1)).shop(shops.get(0)).build()); //warehouse 2 - shop1
        warehouseShopRelations.add(WarehouseShopRelation.builder().warehouse(listWarehouse.get(2)).shop(shops.get(0)).build()); //warehouse 3 - shop1

        warehouseShopRelations.add(WarehouseShopRelation.builder().warehouse(listWarehouse.get(3)).shop(shops.get(1)).build()); //warehouse 4 - shop2
        warehouseShopRelations.add(WarehouseShopRelation.builder().warehouse(listWarehouse.get(4)).shop(shops.get(2)).build()); //warehouse 5 - shop3
        warehouseShopRelations.add(WarehouseShopRelation.builder().warehouse(listWarehouse.get(5)).shop(shops.get(3)).build()); //warehouse 6 - shop4
        warehouseShopRelationRepository.saveAll(warehouseShopRelations);


    }
}
