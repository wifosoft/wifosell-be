package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.product.ProductRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Product>>> getAllRootProducts() {
        List<Product> products = productService.getAllRootProducts();
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Product>>> getRootProductsByUser(
            @CurrentUser UserPrincipal userPrincipal) {
        List<Product> products = productService.getRootProductsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/shop={shopId}/saleChannel={saleChannelId}")
    public ResponseEntity<GApiResponse<List<Product>>> getRootProductsByShopIdAndSaleChannelId(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "shopId") Long shopId,
            @PathVariable(name = "saleChannelId") Long saleChannelId) {
        List<Product> products = productService.getRootProductsByShopIdAndSaleChannelId(shopId, saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/shop={shopId}")
    public ResponseEntity<GApiResponse<List<Product>>> getRootProductsByShopId(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "shopId") Long shopId) {
        List<Product> products = productService.getRootProductsByShopId(shopId);
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/saleChannel={saleChannelId}")
    public ResponseEntity<GApiResponse<List<Product>>> getRootProductsBySaleChannelId(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId) {
        List<Product> products = productService.getRootProductsBySaleChannelId(saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/parent={parentProductId}")
    public ResponseEntity<GApiResponse<List<Product>>> getRootProductsByParentProductId(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "parentProductId") Long parentProductId) {
        List<Product> products = productService.getProductsByParentProductId(parentProductId);
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}")
    public ResponseEntity<GApiResponse<Product>> getProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(GApiResponse.success(product));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<Product>> addProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ProductRequest productRequest) {
        Product product = productService.addProduct(userPrincipal.getId(), productRequest);
        return ResponseEntity.ok(GApiResponse.success(product));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}/update")
    public ResponseEntity<GApiResponse<Product>> updateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId,
            @RequestBody ProductRequest productRequest) {
        Product product = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(GApiResponse.success(product));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}/activate")
    public ResponseEntity<GApiResponse<Product>> activateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId) {
        Product product = productService.activateProduct(productId);
        return ResponseEntity.ok(GApiResponse.success(product));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}/deactivate")
    public ResponseEntity<GApiResponse<Product>> deactivateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId) {
        Product product = productService.deactivateProduct(productId);
        return ResponseEntity.ok(GApiResponse.success(product));
    }
}
