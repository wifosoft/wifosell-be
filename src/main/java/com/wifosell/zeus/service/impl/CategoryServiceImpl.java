package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        this.removeInactiveCategories(categories);
        return categories;
    }

    @Override
    public List<Category> getCategories(Long userId) {
        User gm = this.getGeneralManagerByUserId(userId);
        List<Category> categories = categoryRepository.findCategoriesByGeneralManagerId(gm.getId());
        this.removeInactiveCategories(categories);
        return categories;
    }

    @Override
    public List<Category> getRootCategories(Long userId) {
        User gm = this.getGeneralManagerByUserId(userId);
        return categoryRepository.findCategoriesByGeneralManagerId(gm.getId());
    }

    @Override
    public List<Category> getChildCategories(Long parentCategoryId) {
        return categoryRepository.findCategoriesByParentCategoryId(parentCategoryId);
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.findCategoryById(categoryId);
    }

    @Override
    public Category addCategory(Long userId, CategoryRequest categoryRequest) {
        User gm = this.getGeneralManagerByUserId(userId);
        Category category = new Category();
        this.updateCategoryByRequest(category, categoryRequest);
        category.setGeneralManager(gm);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = this.getCategory(categoryId);
        this.updateCategoryByRequest(category, categoryRequest);
        return categoryRepository.save(category);
    }

    @Override
    public Category activateCategory(Long categoryId) {
        Category category = categoryRepository.findCategoryById(categoryId, true);
        category.setIsActive(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category deactivateCategory(Long categoryId) {
        Category category = categoryRepository.findCategoryById(categoryId);
        category.setIsActive(false);
        return categoryRepository.save(category);
    }

    private User getGeneralManagerByUser(User user) {
        if (user.isRoot())
            return user;
        return this.getGeneralManagerByUser(user.getParent());
    }

    private User getGeneralManagerByUserId(Long userId) {
        User user = userRepository.getUserById(userId);
        return this.getGeneralManagerByUser(user);
    }

    private void updateCategoryByRequest(Category category, CategoryRequest categoryRequest) {
        Optional.ofNullable(categoryRequest.getName()).ifPresent(category::setName);
        Optional.ofNullable(categoryRequest.getDescription()).ifPresent(category::setDescription);
        Optional.ofNullable(categoryRequest.getShortName()).ifPresent(category::setShortName);
        Optional.ofNullable(categoryRequest.getParentCategoryId()).ifPresent(parentCategoryId -> {
            Category parentCategory = categoryRepository.findById(parentCategoryId).orElseThrow(
                    () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PARENT_CATEGORY_NOT_FOUND))
            );
            category.setParent(parentCategory);
        });
    }

    private void removeInactiveCategories(List<Category> categories) {
        List<Category> inactiveCategories = new ArrayList<>();
        categories.forEach(category -> {
            if (category.isActive()) {
                this.removeInactiveCategories(category.getChildren());
            } else {
                inactiveCategories.add(category);
            }
        });
        categories.removeAll(inactiveCategories);
    }
}
