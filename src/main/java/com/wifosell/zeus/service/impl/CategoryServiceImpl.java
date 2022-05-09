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
    public List<Category> getAllRootCategories(Boolean isActive) {
        if (isActive == null)
            return categoryRepository.findAllRoots();
        return categoryRepository.findAllRootsWithActive(isActive);
    }

    @Override
    public List<Category> getRootCategories(@NonNull Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return categoryRepository.findAllRootsWithGm(gm.getId());
        return categoryRepository.findAllRootsWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public Category getCategory(@NonNull Long userId, @NonNull Long categoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return categoryRepository.getByIdWithGm(gm.getId(), categoryId);
    }

    @Override
    public Category addCategory(@NonNull Long userId, @NonNull CategoryRequest categoryRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = new Category();
        return this.updateCategoryByRequest(category, categoryRequest, gm);
    }

    @Override
    public Category updateCategory(@NonNull Long userId, @NonNull Long categoryId, @NonNull CategoryRequest categoryRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = this.getCategory(userId, categoryId);
        return this.updateCategoryByRequest(category, categoryRequest, gm);
    }

    @Override
    public Category activateCategory(@NonNull Long userId, @NonNull Long categoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = categoryRepository.getByIdWithGm(gm.getId(), categoryId);
        category.setIsActive(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category deactivateCategory(@NonNull Long userId, @NonNull Long categoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = categoryRepository.getByIdWithGm(gm.getId(), categoryId);
        category.setIsActive(false);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> activateCategories(@NonNull Long userId, @NonNull List<Long> categoryIds) {
        return categoryIds.stream().map(id -> this.activateCategory(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Category> deactivateCategories(@NonNull Long userId, @NonNull List<Long> categoryIds) {
        return categoryIds.stream().map(id -> this.deactivateCategory(userId, id)).collect(Collectors.toList());
    }

    private Category updateCategoryByRequest(@NonNull Category category, @NonNull CategoryRequest categoryRequest, @NonNull User gm) {
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
        return categoryRepository.save(category);
    }
}
