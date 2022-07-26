package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.service.CategoryService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class CategorySeeder extends BaseSeeder implements ISeeder {
    private CategoryService categoryService;

    @Override
    public void prepareJpaRepository() {
        categoryService = context.getBean(CategoryService.class);
    }

    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/category.json");
            CategoryRequest[] requests = new ObjectMapper().readValue(file, CategoryRequest[].class);
            file.close();
            for (CategoryRequest request : requests) {
                categoryService.addCategory(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
