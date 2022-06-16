package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.common.SearchRequest;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.payload.response.product.ProductResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.service.WarehouseService;
import com.wifosell.zeus.utils.paging.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final WarehouseService warehouseService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<Page<ProductResponse>>> getAllProducts(
//            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
//            @RequestParam(name = "saleChannelId", required = false) List<Long> saleChannelIds,
            @RequestParam(name = "warehouseId", required = false) List<Long> warehouseIds,
            @RequestParam(name = "minQuantity", required = false) Integer minQuantity,
            @RequestParam(name = "maxQuantity", required = false) Integer maxQuantity,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
//        if (shopIds != null || saleChannelIds != null) {
//            List<Long> anotherWarehouseIds =
//                    warehouseService.getWarehousesByShopIdsAndSaleChannelIds(shopIds, saleChannelIds).stream()
//                            .map(Warehouse::getId).collect(Collectors.toList());
//            warehouseIds = warehouseIds == null ?
//                    anotherWarehouseIds :
//                    anotherWarehouseIds.stream().filter(warehouseIds::contains).collect(Collectors.toList());
//        }
        Page<Product> products = productService.getProducts(
                null, warehouseIds, minQuantity, maxQuantity, isActives, offset, limit, sortBy, orderBy);
        Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, null, warehouseIds, minQuantity, maxQuantity));
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<ProductResponse>>> getProducts(
            @CurrentUser UserPrincipal userPrincipal,
//            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
//            @RequestParam(name = "saleChannelId", required = false) List<Long> saleChannelIds,
            @RequestParam(name = "warehouseId", required = false) List<Long> warehouseIds,
            @RequestParam(name = "minQuantity", required = false) Integer minQuantity,
            @RequestParam(name = "maxQuantity", required = false) Integer maxQuantity,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
//        if (shopIds != null || saleChannelIds != null) {
//            List<Long> anotherWarehouseIds = warehouseService.getWarehousesByShopIdsAndSaleChannelIds(
//                    shopIds, saleChannelIds).stream().map(Warehouse::getId).collect(Collectors.toList());
//            warehouseIds = warehouseIds == null ?
//                    anotherWarehouseIds :
//                    anotherWarehouseIds.stream().filter(warehouseIds::contains).collect(Collectors.toList());
//        }
        Page<Product> products = productService.getProducts(
                userPrincipal.getId(), warehouseIds, minQuantity, maxQuantity, isActives, offset, limit, sortBy, orderBy);
        Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, null, warehouseIds, minQuantity, maxQuantity));
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/search")
    public ResponseEntity<GApiResponse<PageInfo<ProductResponse>>> searchProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid SearchRequest request,
            @RequestParam(name = "warehouseId", required = false) List<Long> warehouseIds,
            @RequestParam(name = "minQuantity", required = false) Integer minQuantity,
            @RequestParam(name = "maxQuantity", required = false) Integer maxQuantity,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        PageInfo<Product> products = productService.searchProducts(
                userPrincipal.getId(), request.getKeyword(), warehouseIds, minQuantity, maxQuantity, isActives, offset, limit);
        PageInfo<ProductResponse> responses = products
                .map(product -> new ProductResponse(product, request.getKeyword(), warehouseIds, minQuantity, maxQuantity));
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}")
    public ResponseEntity<GApiResponse<ProductResponse>> getProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId
    ) {
        Product product = productService.getProduct(userPrincipal.getId(), productId);
        ProductResponse response = new ProductResponse(product);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<ProductResponse>> addProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid AddProductRequest request
    ) {
        Product product = productService.addProduct(userPrincipal.getId(), request);
        ProductResponse response = new ProductResponse(product);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}/update")
    public ResponseEntity<GApiResponse<ProductResponse>> updateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId,
            @RequestBody @Valid UpdateProductRequest request
    ) {
        Product product = productService.updateProduct(userPrincipal.getId(), productId, request);
        ProductResponse response = new ProductResponse(product);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}/activate")
    public ResponseEntity<GApiResponse<Product>> activateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId
    ) {
        Product product = productService.activateProduct(userPrincipal.getId(), productId);
        return ResponseEntity.ok(GApiResponse.success(product));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}/deactivate")
    public ResponseEntity<GApiResponse<Product>> deactivateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId
    ) {
        Product product = productService.deactivateProduct(userPrincipal.getId(), productId);
        return ResponseEntity.ok(GApiResponse.success(product));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<Product>>> activateProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<Product> products = productService.activateProducts(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<Product>>> deactivateProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<Product> products = productService.deactivateProducts(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(products));
    }
}
