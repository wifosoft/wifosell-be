package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.shop.AddShopRequest;
import com.wifosell.zeus.service.Shop2Service;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class ShopSeeder extends BaseSeeder implements ISeeder {
    private Shop2Service shopService;

    @Override
    public void prepareJpaRepository() {
        shopService = context.getBean(Shop2Service.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/shop.json");
            AddShopRequest[] requests = new ObjectMapper().readValue(file, AddShopRequest[].class);
            file.close();
            for (AddShopRequest request : requests) {
                shopService.addShop(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
