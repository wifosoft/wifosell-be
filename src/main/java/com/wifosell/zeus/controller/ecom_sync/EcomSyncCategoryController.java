package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.ecom_sync.EcomSyncCategoryRequest;
import com.wifosell.zeus.payload.response.ecom_sync.EcomSyncCategoryResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomSyncCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ecom_sync/category")
public class EcomSyncCategoryController {
    private final EcomSyncCategoryService ecomSyncCategoryService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<EcomSyncCategoryResponse>> linkCategories(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid EcomSyncCategoryRequest request
    ) {
        EcomSyncCategoryResponse response = ecomSyncCategoryService.linkEcomCategoriesAndSysCategory(
                userPrincipal.getId(), request.getSysCategoryId(), request.getLazadaCategoryId(), request.getSendoCategoryId());
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<EcomSyncCategoryResponse>>> getLinks(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        List<EcomSyncCategoryResponse> response = ecomSyncCategoryService.getLinks(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{sysCategoryId}")
    public ResponseEntity<GApiResponse<EcomSyncCategoryResponse>> getLink(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "sysCategoryId") Long sysCategoryId
    ) {
        EcomSyncCategoryResponse response = ecomSyncCategoryService.getLink(
                userPrincipal.getId(), sysCategoryId);
        return ResponseEntity.ok(GApiResponse.success(response));
    }
}
