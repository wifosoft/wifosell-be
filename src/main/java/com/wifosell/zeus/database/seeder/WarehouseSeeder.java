package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.service.WarehouseService;
import com.wifosell.zeus.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class WarehouseSeeder extends BaseSeeder implements ISeeder {
    private WarehouseService warehouseService;

    @Override
    public void prepareJpaRepository() {
        warehouseService = context.getBean(WarehouseService.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/warehouse.json");
            WarehouseRequest[] requests = new ObjectMapper().readValue(file, WarehouseRequest[].class);
            file.close();
            for (WarehouseRequest request : requests) {
                warehouseService.addWarehouse(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
