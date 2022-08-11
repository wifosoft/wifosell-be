package com.wifosell.zeus.database.seeder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryTreePayload;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAndSysCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryRepository;
import com.wifosell.zeus.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LazadaCategorySeeder extends BaseSeeder implements ISeeder {
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private LazadaCategoryRepository lazadaCategoryRepository;

    private LazadaCategoryAndSysCategoryRepository lazadaCategoryAndSysCategoryRepository;


    @Override
    public void prepareJpaRepository() {
        this.categoryRepository = context.getBean(CategoryRepository.class);
        this.userRepository = context.getBean(UserRepository.class);
        this.lazadaCategoryRepository = context.getBean(LazadaCategoryRepository.class);
        this.lazadaCategoryAndSysCategoryRepository = context.getBean(LazadaCategoryAndSysCategoryRepository.class);
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
        try {
            Gson gson = new Gson();
            InputStream file = (new FileUtils()).getFileAsIOStream("data/lazada_category_response_payload.json");

            JsonReader reader = new JsonReader(new InputStreamReader(file));
            ResponseCategoryTreePayload responseTokenPayload = gson.fromJson(reader, ResponseCategoryTreePayload.class);
            file.close();

            List<ResponseCategoryTreePayload.CategoryTreeItem> categoryTreeItemList = responseTokenPayload.getData();
            for (ResponseCategoryTreePayload.CategoryTreeItem item : categoryTreeItemList) {
                saveLazadaCategory(item, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Category category1 = categoryRepository.findById(1L).orElse(null);
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        LazadaCategory lazadaCategory1682 = lazadaCategoryRepository.findById(1682L).orElse(null);
        LazadaCategory lazadaCategory1684 = lazadaCategoryRepository.findById(1684L).orElse(null);
        LazadaCategory lazadaCategory1680 = lazadaCategoryRepository.findById(1680L).orElse(null);

        List<LazadaCategory> listLazadaCategory = new ArrayList<>();
        listLazadaCategory.add(lazadaCategory1682);
        listLazadaCategory.add(lazadaCategory1684);
        listLazadaCategory.add(lazadaCategory1680);


        for (LazadaCategory lc : listLazadaCategory) {
            LazadaCategoryAndSysCategory linkCategory = new LazadaCategoryAndSysCategory();
            linkCategory.setLazadaCategory(lc);
            linkCategory.setSysCategory(category1);
            linkCategory.setGeneralManager(gm);
            lazadaCategoryAndSysCategoryRepository.save(linkCategory);
        }
    }
}
