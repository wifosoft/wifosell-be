package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')") // TODO haukc
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Category>>> getAllCategories() {
        List<Category> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(GApiResponse.success(categoryList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Category>>> getCategories(@CurrentUser UserPrincipal userPrincipal) {
        List<Category> categoryList = categoryService.getCategories(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(categoryList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}")
    public ResponseEntity<GApiResponse<Category>> getCategory(@CurrentUser UserPrincipal userPrincipal,
                                                              @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(GApiResponse.success(category));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<Category>> addCategory(@CurrentUser UserPrincipal userPrincipal,
                                                    @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(userPrincipal.getId(), categoryRequest);
        return ResponseEntity.ok(GApiResponse.success(category));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{categoryId}")
    public ResponseEntity<GApiResponse<Category>> updateCategory(@CurrentUser UserPrincipal userPrincipal,
                                                       @PathVariable(name = "categoryId") Long categoryId,
                                                       @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.updateCategory(categoryId, categoryRequest);
        return ResponseEntity.ok(GApiResponse.success(category));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}/activate")
    public ResponseEntity<GApiResponse<Category>> activateCategory(@CurrentUser UserPrincipal userPrincipal,
                                                         @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.activateCategory(categoryId);
        return ResponseEntity.ok(GApiResponse.success(category));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}/deactivate")
    public ResponseEntity<GApiResponse<Category>> deactivateCategory(@CurrentUser UserPrincipal userPrincipal,
                                                           @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.deactivateCategory(categoryId);
        return ResponseEntity.ok(GApiResponse.success(category));
    }
}
