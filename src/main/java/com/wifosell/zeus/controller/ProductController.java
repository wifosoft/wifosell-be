package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.payload.response.product.ProductResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.ProductService;
import com.wifosell.zeus.utils.Preprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(name = "isActive", required = false) List<Boolean> actives,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "orderBy", required = false, defaultValue = "asc") String orderBy
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        Page<ProductResponse> responses = productService.getAllProducts(isActive, offset, limit, sortBy, orderBy)
                .map(ProductResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<ProductResponse>>> getProducts(
            @CurrentUser UserPrincipal userPrincipal,
//            @RequestParam(name = "shopId", required = false) Long shopId,
            @RequestParam(name = "isActive", required = false) List<Boolean> actives,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "orderBy", required = false, defaultValue = "asc") String orderBy
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        Page<Product> products = productService.getProducts(userPrincipal.getId(), isActive, offset, limit, sortBy, orderBy);
        Page<ProductResponse> responses = products.map(ProductResponse::new);
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
            @RequestBody AddProductRequest request
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
            @RequestBody UpdateProductRequest request
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
            @RequestBody ListIdRequest request
    ) {
        List<Product> products = productService.activateProducts(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<Product>>> deactivateProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Product> products = productService.deactivateProducts(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(products));
    }
}
