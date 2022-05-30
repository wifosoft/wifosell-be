package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.variant.AddVariantRequest;
import com.wifosell.zeus.payload.request.variant.UpdateVariantRequest;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/variants")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<Page<VariantResponse>>> getAllVariants(
            @RequestParam(name = "warehouseId", required = false) List<Long> warehouseIds,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<Variant> variants = variantService.getVariants(
                null, warehouseIds, isActives, offset, limit, sortBy, orderBy);
        Page<VariantResponse> responses = variants.map(VariantResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<VariantResponse>>> getVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "warehouseId", required = false) List<Long> warehouseIds,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<Variant> variants = variantService.getVariants(
                userPrincipal.getId(), warehouseIds, isActives, offset, limit, sortBy, orderBy);
        Page<VariantResponse> responses = variants.map(VariantResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{variantId}")
    public ResponseEntity<GApiResponse<VariantResponse>> getVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "variantId") Long variantId
    ) {
        Variant variant = variantService.getVariant(userPrincipal.getId(), variantId);
        VariantResponse response = new VariantResponse(variant);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<VariantResponse>> addVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid AddVariantRequest request
    ) {
        Variant variant = variantService.addVariant(userPrincipal.getId(), request);
        VariantResponse response = new VariantResponse(variant);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{variantId}/update")
    public ResponseEntity<GApiResponse<VariantResponse>> updateVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "variantId") Long variantId,
            @RequestBody @Valid UpdateVariantRequest request
    ) {
        Variant variant = variantService.updateVariant(userPrincipal.getId(), variantId, request);
        VariantResponse response = new VariantResponse(variant);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{variantId}/activate")
    public ResponseEntity<GApiResponse<Variant>> activateVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "variantId") Long variantId
    ) {
        Variant variant = variantService.activateVariant(userPrincipal.getId(), variantId);
        return ResponseEntity.ok(GApiResponse.success(variant));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{variantId}/deactivate")
    public ResponseEntity<GApiResponse<Variant>> deactivateVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "variantId") Long variantId
    ) {
        Variant variant = variantService.deactivateVariant(userPrincipal.getId(), variantId);
        return ResponseEntity.ok(GApiResponse.success(variant));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<Variant>>> activateVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<Variant> variants = variantService.activateVariants(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(variants));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<Variant>>> deactivateVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<Variant> variants = variantService.deactivateVariants(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(variants));
    }
}
