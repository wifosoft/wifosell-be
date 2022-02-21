package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.category.ActivateCategoriesRequest;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.payload.request.category.DeactivateCategoriesRequest;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')") // TODO haukc
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categoryList = categoryService.getAllCategories();
        List<CategoryResponse> categoryResponses = categoryList
                .stream().map(CategoryResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(categoryResponses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<CategoryResponse>>> getCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "parentId", required = false) Long parentId) {
        List<Category> categoryList = categoryService.getCategories(userPrincipal.getId(), parentId);
        List<CategoryResponse> categoryResponses = categoryList
                .stream().map(CategoryResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(categoryResponses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}")
    public ResponseEntity<GApiResponse<CategoryResponse>> getCategory(@CurrentUser UserPrincipal userPrincipal,
                                                              @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.getCategory(categoryId);
        CategoryResponse categoryResponse = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(categoryResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<CategoryResponse>> addCategory(@CurrentUser UserPrincipal userPrincipal,
                                                    @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(userPrincipal.getId(), categoryRequest);
        CategoryResponse categoryResponse = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(categoryResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{categoryId}/update")
    public ResponseEntity<GApiResponse<CategoryResponse>> updateCategory(@CurrentUser UserPrincipal userPrincipal,
                                                       @PathVariable(name = "categoryId") Long categoryId,
                                                       @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.updateCategory(categoryId, categoryRequest);
        CategoryResponse categoryResponse = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(categoryResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}/activate")
    public ResponseEntity<GApiResponse<CategoryResponse>> activateCategory(@CurrentUser UserPrincipal userPrincipal,
                                                         @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.activateCategory(categoryId);
        CategoryResponse categoryResponse = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(categoryResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}/deactivate")
    public ResponseEntity<GApiResponse<CategoryResponse>> deactivateCategory(@CurrentUser UserPrincipal userPrincipal,
                                                           @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.deactivateCategory(categoryId);
        CategoryResponse categoryResponse = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(categoryResponse));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<CategoryResponse>>> activateCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ActivateCategoriesRequest request) {
        List<Category> categoryList = categoryService.activateCategories(request.getIds());
        List<CategoryResponse> categoryResponses = categoryList.stream()
                .map(CategoryResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(categoryResponses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<CategoryResponse>>> deactivateCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody DeactivateCategoriesRequest request) {
        List<Category> categoryList = categoryService.deactivateCategories(request.getIds());
        List<CategoryResponse> categoryResponses = categoryList.stream()
                .map(CategoryResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(categoryResponses));
    }
}
