package com.wifosell.zeus.service;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.request.category.CategoryRequest;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getCategories(Long userId, Long parentCategoryId);
    Category getCategory(Long categoryId);
    Category addCategory(Long userId, CategoryRequest categoryRequest);
    Category updateCategory(Long categoryId, CategoryRequest categoryRequest);
    Category activateCategory(Long categoryId);
    Category deactivateCategory(Long categoryId);
    List<Category> activateCategories(List<Long> categoryIds);
    List<Category> deactivateCategories(List<Long> categoryIds);
}
