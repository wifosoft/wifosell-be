package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.provider.lazada.report.FetchAndSyncLazadaProductsReport;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.LazadaProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ecom_sync/lazada/product")
@RequiredArgsConstructor
public class LazadaProductController {
    private final LazadaProductService lazadaProductService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/fetch")
    public ResponseEntity<GApiResponse<FetchAndSyncLazadaProductsReport>> fetchLazadaProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId") Long ecomId
    ) {
        FetchAndSyncLazadaProductsReport report = lazadaProductService.fetchAndSyncLazadaProducts(
                userPrincipal.getId(), ecomId);
        return ResponseEntity.ok(GApiResponse.success(report));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<LazadaProduct>>> getLazadaProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId") Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<LazadaProduct> products = lazadaProductService.getLazadaProducts(
                userPrincipal.getId(), ecomId, offset, limit, sortBy, orderBy);
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<GApiResponse<LazadaProduct>> getLazadaProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "id") Long id
    ) {
        LazadaProduct lazadaProduct = lazadaProductService.getLazadaProduct(userPrincipal.getId(), id);
        return ResponseEntity.ok(GApiResponse.success(lazadaProduct));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/variant")
    public ResponseEntity<GApiResponse<Page<LazadaVariant>>> getLazadaVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId") Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<LazadaVariant> variants = lazadaProductService.getLazadaVariants(
                userPrincipal.getId(), ecomId, offset, limit, sortBy, orderBy);
        return ResponseEntity.ok(GApiResponse.success(variants));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/variant/{id}")
    public ResponseEntity<GApiResponse<LazadaVariant>> getLazadaVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "id") Long id
    ) {
        LazadaVariant lazadaVariant = lazadaProductService.getLazadaVariant(userPrincipal.getId(), id);
        return ResponseEntity.ok(GApiResponse.success(lazadaVariant));
    }
}
