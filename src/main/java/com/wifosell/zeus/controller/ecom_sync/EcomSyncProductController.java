package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.ecom_sync.EcomSyncUpdateStockRequest;
import com.wifosell.zeus.payload.response.ecom_sync.EcomSyncUpdateStockResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomSyncProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("api/ecom_sync/product")
public class EcomSyncProductController {
    private final EcomSyncProductService ecomSyncProductService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update_stock")
    public ResponseEntity<GApiResponse<EcomSyncUpdateStockResponse>> updateStock(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid EcomSyncUpdateStockRequest request
    ) {
        EcomSyncUpdateStockResponse response = ecomSyncProductService.updateStock(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(response));
    }
}
