package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class ProductSeeder extends BaseSeeder implements ISeeder {
    private ProductService productService;

    @Override
    public void prepareJpaRepository() {
        productService = context.getBean(ProductService.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/product.json");
            AddProductRequest[] requests = new ObjectMapper().readValue(file, AddProductRequest[].class);
            file.close();
            for (AddProductRequest request : requests) {
                productService.addProduct(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
