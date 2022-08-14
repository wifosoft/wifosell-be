package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.SendoProductService;
import com.wifosell.zeus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ecom_sync/sendo")
public class SendoProductController {
    @Autowired
    EcomService ecomService;

    @Autowired
    UserService userService;

    @Autowired
    SendoProductService sendoProductService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("product_list")
    public ResponseEntity<GApiResponse<Page<SendoProduct>>> getLazadaProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<SendoProduct> products = sendoProductService.getProducts(ecomId, offset, limit, sortBy, orderBy);
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("variant_list")
    public ResponseEntity<GApiResponse<Page<SendoVariant>>> getLazadaVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<SendoVariant> variants = sendoProductService.getVariants(ecomId, offset, limit, sortBy, orderBy);
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        return ResponseEntity.ok(GApiResponse.success(variants));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("publishSendoProduct")
    public ResponseEntity<GApiResponse> postSendoProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "productId", required = false) Long productId
    ) {
        var response = sendoProductService.pulishCreateSystemProductToSendo(ecomId, productId);
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        return ResponseEntity.ok(GApiResponse.success(response));
    }
    
    


}
