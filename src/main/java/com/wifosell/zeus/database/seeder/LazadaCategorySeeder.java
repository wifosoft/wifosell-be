package com.wifosell.zeus.database.seeder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryTreePayload;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class LazadaCategorySeeder extends BaseSeeder implements ISeeder {
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private LazadaCategoryRepository lazadaCategoryRepository;

    @Override
    public void prepareJpaRepository() {
        this.categoryRepository = this.factory.getRepository(CategoryRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
        this.lazadaCategoryRepository = this.factory.getRepository(LazadaCategoryRepository.class);
    }

    public void saveLazadaCategory(ResponseCategoryTreePayload.CategoryTreeItem categoryTreeItem, LazadaCategory parent) {
        LazadaCategory lazadaCategory = new LazadaCategory(categoryTreeItem, parent);
        lazadaCategoryRepository.save(lazadaCategory);
        if (categoryTreeItem.getChildren() != null) {
            for (ResponseCategoryTreePayload.CategoryTreeItem item : categoryTreeItem.getChildren()) {
                saveLazadaCategory(item, lazadaCategory);
            }

        }
    }


    @Override
    public void run() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("src/main/java/com/wifosell/zeus/database/data/lazada_category_response_payload.json"));
        ResponseCategoryTreePayload responseTokenPayload = gson.fromJson(reader, ResponseCategoryTreePayload.class);

        List<ResponseCategoryTreePayload.CategoryTreeItem> categoryTreeItemList = responseTokenPayload.getData();
        for (ResponseCategoryTreePayload.CategoryTreeItem item : categoryTreeItemList) {
            saveLazadaCategory(item, null);
        }
    }
}
