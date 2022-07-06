package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAttribute;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAttributeRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class LazadaCategoryAttributeSeeder extends BaseSeeder implements ISeeder {

    private LazadaCategoryAttributeRepository lazadaCategoryAttributeRepository;

    @Override
    public void prepareJpaRepository() {

        this.lazadaCategoryAttributeRepository = this.factory.getRepository(LazadaCategoryAttributeRepository.class);
    }


    @Override
    public void run() throws FileNotFoundException {
        try {
            File file = new File("src/main/resources/data/lazada_category_attribute_payload.json");
            LazadaCategoryAttribute[] categoryAttributes = new ObjectMapper().readValue(file, LazadaCategoryAttribute[].class);
            lazadaCategoryAttributeRepository.saveAll(Arrays.asList(categoryAttributes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
