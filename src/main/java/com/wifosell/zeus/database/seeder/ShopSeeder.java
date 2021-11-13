package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.UserShopRelationRepository;
import com.wifosell.zeus.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

public class ShopSeeder extends BaseSeeder implements ISeeder{
    UserRepository userRepository;

    ShopRepository shopRepository;


    UserShopRelationRepository userShopRelationRepository;

    @Override
    public void prepareJpaRepository() {
        userRepository = factory.getRepository(UserRepository.class);
        shopRepository = factory.getRepository(ShopRepository.class);
        userShopRelationRepository = factory.getRepository(UserShopRelationRepository.class);
    }

    @Override
    public void run() {
        User manager1 =userRepository.getUserByName("manager1");
        User manager2 =userRepository.getUserByName("manager2");

        Shop shop1 = Shop.builder().name("Quản lý 1 - Cửa hàng 1")
                .shortName("CH1-QL1")
                .address("Đông Hưng Thái Bình")
                .phone("0982245445")
                .businessLine("Mỹ phẩm")
                .generalManager(manager1).build();

        Shop shop2 = Shop.builder()
                .name("Quản lý 1 - Cửa hàng 2 ")
                .shortName("CH2-QL1")
                .address("Quận 5, Hồ Chí Minh")
                .phone("123123")
                .businessLine("Mỹ phẩm")
                .generalManager(manager1).build();

        Shop shop3 = Shop.builder().name("Quản lý 2 - Cửa hàng 1")
                .shortName("QL2-CH1")
                .address("Đông Hưng Thái Bình")
                .phone("0982245445")
                .businessLine("Siêu thị")
                .generalManager(manager2).build();

        Shop shop4 = Shop.builder().name("Quản lý 2 - Cửa hàng 2")
                .shortName("QL2-CH2")
                .address("Bình chánh Hồ Chí Minh")
                .phone("01285544888")
                .businessLine("Điện tử")
                .generalManager(manager2).build();


        List<Shop> shops = new ArrayList<>();
        shops.add(shop1);
        shops.add(shop2);
        shops.add(shop3);
        shops.add(shop4);
        shopRepository.saveAll(shops);

        //add permission to shop

        List<UserShopRelation> listUserShopRelation = new ArrayList<>();
        listUserShopRelation.add(UserShopRelation.builder().shop(shop1).user(manager1).build());
        listUserShopRelation.add(UserShopRelation.builder().shop(shop2).user(manager1).build());
        listUserShopRelation.add(UserShopRelation.builder().shop(shop3).user(manager2).build());
        listUserShopRelation.add(UserShopRelation.builder().shop(shop4).user(manager2).build());
        userShopRelationRepository.saveAll(listUserShopRelation);

    }
}
