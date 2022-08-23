package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.service.SupplierService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class SupplierSeeder extends BaseSeeder implements ISeeder {
    private SupplierService supplierService;

    @Override
    public void prepareJpaRepository() {
        supplierService = context.getBean(SupplierService.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/supplier.json");
            AddSupplierRequest[] requests = new ObjectMapper().readValue(file, AddSupplierRequest[].class);
            file.close();
            for (AddSupplierRequest request : requests) {
                supplierService.addSupplier(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
