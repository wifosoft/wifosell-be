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
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategories(Long userId, Long parentCategoryId) {
        if (parentCategoryId == null) {
            User gm = userRepository.getUserById(userId).getGeneralManager();
            return categoryRepository.findCategoriesByGeneralManagerId(gm.getId());
        } else {
            return categoryRepository.findCategoriesByParentCategoryId(parentCategoryId);
        }
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.findCategoryById(categoryId);
    }

    @Override
    public Category addCategory(Long userId, CategoryRequest categoryRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
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

    @Override
    public List<Category> activateCategories(List<Long> categoryIds) {
        return categoryIds.stream().map(this::activateCategory).collect(Collectors.toList());
    }

    @Override
    public List<Category> deactivateCategories(List<Long> categoryIds) {
        return categoryIds.stream().map(this::deactivateCategory).collect(Collectors.toList());
    }

    private void updateCategoryByRequest(@NonNull Category category, @NonNull CategoryRequest categoryRequest) {
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
}
