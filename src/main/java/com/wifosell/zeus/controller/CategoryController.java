package com.wifosell.zeus.controller;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.payload.request.category.SysCategoryLinkEcomCategoryRequest;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import com.wifosell.zeus.payload.response.category.GetCategoriesResponse;
import com.wifosell.zeus.payload.response.category.GetCategoryResponse;
import com.wifosell.zeus.payload.response.category.SysCategoryLinkEcomCategoryResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.CategoryService;
import com.wifosell.zeus.utils.Preprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
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

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<GetCategoriesResponse>>> getAllRootCategories(
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Category> categories = categoryService.getAllRootCategories(isActive);
        List<GetCategoriesResponse> response = categories.stream()
                .map(c -> new GetCategoriesResponse(c, isActive))
                .collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<GetCategoriesResponse>>> getRootCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Category> categories = categoryService.getRootCategories(userPrincipal.getId(), isActive);
        List<GetCategoriesResponse> response = categories.stream()
                .map(c -> new GetCategoriesResponse(c, isActive))
                .collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}")
    public ResponseEntity<GApiResponse<GetCategoryResponse>> getCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.getCategory(userPrincipal.getId(), categoryId);
        SysCategoryLinkEcomCategoryResponse.LinkItemResponse linkItem = categoryService.getSingleLinkCategoryEcomCategory(userPrincipal.getId(), category);

        GetCategoryResponse response = new GetCategoryResponse(category,linkItem);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<CategoryResponse>> addCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(userPrincipal.getId(), categoryRequest);
        SysCategoryLinkEcomCategoryResponse.LinkItemResponse linkItem = categoryService.getSingleLinkCategoryEcomCategory(userPrincipal.getId(), category);
        CategoryResponse response = new CategoryResponse(category, linkItem);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{categoryId}/update")
    public ResponseEntity<GApiResponse<CategoryResponse>> updateCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = categoryService.updateCategory(userPrincipal.getId(), categoryId, categoryRequest);
        SysCategoryLinkEcomCategoryResponse.LinkItemResponse linkItem = categoryService.getSingleLinkCategoryEcomCategory(userPrincipal.getId(), category);
        CategoryResponse response = new CategoryResponse(category, linkItem);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}/activate")
    public ResponseEntity<GApiResponse<CategoryResponse>> activateCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.activateCategory(userPrincipal.getId(), categoryId);
        CategoryResponse response = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{categoryId}/deactivate")
    public ResponseEntity<GApiResponse<CategoryResponse>> deactivateCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "categoryId") Long categoryId) {
        Category category = categoryService.deactivateCategory(userPrincipal.getId(), categoryId);
        CategoryResponse response = new CategoryResponse(category);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<CategoryResponse>>> activateCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request) {
        List<Category> categoryList = categoryService.activateCategories(userPrincipal.getId(), request.getIds());
        List<CategoryResponse> response = categoryList.stream()
                .map(CategoryResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<CategoryResponse>>> deactivateCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request) {
        List<Category> categoryList = categoryService.deactivateCategories(userPrincipal.getId(), request.getIds());
        List<CategoryResponse> response = categoryList.stream()
                .map(CategoryResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/linkCategory")
    public ResponseEntity<GApiResponse> getLinkCategory(@CurrentUser UserPrincipal userPrincipal){
        var resp  = categoryService.getAllLinkCategoryEcomCategory(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(resp));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/linkCategory")
    public ResponseEntity<GApiResponse<Boolean>> linkCategory(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid SysCategoryLinkEcomCategoryRequest request) {
        categoryService.linkCategoryEcomCategory(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

}
