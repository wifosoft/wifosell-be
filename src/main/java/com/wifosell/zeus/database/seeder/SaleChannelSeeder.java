package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.service.SaleChannelService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class SaleChannelSeeder extends BaseSeeder implements ISeeder {
    private SaleChannelService saleChannelService;

    @Override
    public void prepareJpaRepository() {
        saleChannelService = context.getBean(SaleChannelService.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/sale_channel.json");
            SaleChannelRequest[] requests = new ObjectMapper().readValue(file, SaleChannelRequest[].class);
            file.close();
            for (SaleChannelRequest request : requests) {
                saleChannelService.addSaleChannel(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
