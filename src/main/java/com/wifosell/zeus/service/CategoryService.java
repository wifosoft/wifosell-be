package com.wifosell.zeus.service;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.request.category.CategoryRequest;

import java.util.List;

public interface CategoryService {
    List<Category> getAllRootCategories(Boolean isActive);

    List<Category> getRootCategories(Long userId, Boolean isActive);

    Category getCategory(Long userId, Long categoryId);

    Category addCategory(Long userId, CategoryRequest categoryRequest);

    Category updateCategory(Long userId, Long categoryId, CategoryRequest categoryRequest);

    Category activateCategory(Long userId, Long categoryId);

    Category deactivateCategory(Long userId, Long categoryId);

    List<Category> activateCategories(Long userId, List<Long> categoryIds);

    List<Category> deactivateCategories(Long userId, List<Long> categoryIds);
}
