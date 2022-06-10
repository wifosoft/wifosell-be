package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAttribute;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryTreePayload;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAttributeRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LazadaCategoryAttributeSeeder extends BaseSeeder implements ISeeder {

    private LazadaCategoryAttributeRepository lazadaCategoryAttributeRepository;
    @Override
    public void prepareJpaRepository() {

        this.lazadaCategoryAttributeRepository = this.factory.getRepository(LazadaCategoryAttributeRepository.class);
    }


    @Override
    public void run() throws FileNotFoundException {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/com/wifosell/zeus/database/data/lazada_category_attribute_payload.json");

        try {
            LazadaCategoryAttribute[] categoryAttributes  = mapper.readValue(file, LazadaCategoryAttribute[].class);
            lazadaCategoryAttributeRepository.saveAll(Arrays.asList(categoryAttributes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
