package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class CategorySeeder extends BaseSeeder implements ISeeder {
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        this.categoryRepository = this.factory.getRepository(CategoryRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/category.json");
            CategoryRequest[] requests = new ObjectMapper().readValue(file, CategoryRequest[].class);
            file.close();
            for (CategoryRequest request : requests) {
                this.addCategoryByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCategoryByRequest(CategoryRequest categoryRequest, User gm) {
        Category category = new Category();
        Optional.ofNullable(categoryRequest.getName()).ifPresent(category::setName);
        Optional.ofNullable(categoryRequest.getDescription()).ifPresent(category::setDescription);
        Optional.ofNullable(categoryRequest.getShortName()).ifPresent(category::setShortName);
        Optional.ofNullable(categoryRequest.getParentCategoryId()).ifPresent(parentCategoryId -> {
            Category parentCategory = categoryRepository.findByIdWithGmAndActive(gm.getId(), parentCategoryId, true).orElseThrow(
                    () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PARENT_CATEGORY_NOT_FOUND))
            );
            category.setParent(parentCategory);
        });
        Optional.ofNullable(categoryRequest.getIsActive()).ifPresent(category::setIsActive);
        category.setGeneralManager(gm);
        categoryRepository.save(category);
    }
}
