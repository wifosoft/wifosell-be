package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.LazadaProductService;
import com.wifosell.zeus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ecom_sync/lazada")
public class LazadaProductController {
    @Autowired
    EcomService ecomService;

    @Autowired
    UserService userService;

    @Autowired
    LazadaProductService lazadaProductService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("product_list")
    public ResponseEntity<GApiResponse<Page<LazadaProduct>>> getLazadaProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<LazadaProduct> products = lazadaProductService.getProducts(ecomId, offset, limit, sortBy, orderBy);
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("variant_list")
    public ResponseEntity<GApiResponse<Page<LazadaVariant>>> getLazadaVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<LazadaVariant> variants = lazadaProductService.getVariants(ecomId, offset, limit, sortBy, orderBy);
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        return ResponseEntity.ok(GApiResponse.success(variants));
    }


}
