package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAttribute;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAttributeRepository;
import com.wifosell.zeus.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class LazadaCategoryAttributeSeeder extends BaseSeeder implements ISeeder {

    private LazadaCategoryAttributeRepository lazadaCategoryAttributeRepository;

    @Override
    public void prepareJpaRepository() {

        this.lazadaCategoryAttributeRepository = context.getBean(LazadaCategoryAttributeRepository.class);
    }


    @Override
    public void run() throws FileNotFoundException {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/lazada_category_attribute_payload.json");
            LazadaCategoryAttribute[] categoryAttributes = new ObjectMapper().readValue(file, LazadaCategoryAttribute[].class);
            file.close();
            lazadaCategoryAttributeRepository.saveAll(Arrays.asList(categoryAttributes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
