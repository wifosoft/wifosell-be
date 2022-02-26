package com.wifosell.zeus.service;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import lombok.NonNull;

import java.util.List;

public interface CategoryService {
    List<Category> getAllRootCategories(Boolean isActive);
    List<Category> getRootCategories(@NonNull Long userId, Boolean isActive);
    Category getCategory(@NonNull Long userId, @NonNull Long categoryId);
    Category addCategory(@NonNull Long userId, @NonNull CategoryRequest categoryRequest);
    Category updateCategory(@NonNull Long userId, @NonNull Long categoryId, @NonNull CategoryRequest categoryRequest);
    Category activateCategory(@NonNull Long userId, @NonNull Long categoryId);
    Category deactivateCategory(@NonNull Long userId, @NonNull Long categoryId);
    List<Category> activateCategories(@NonNull Long userId, @NonNull List<Long> categoryIds);
    List<Category> deactivateCategories(@NonNull Long userId, @NonNull List<Long> categoryIds);
}
