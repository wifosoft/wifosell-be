package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.SendoProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ecom_sync/sendo/product")
@RequiredArgsConstructor
public class SendoProductController {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private final SendoProductService sendoProductService;

    @Autowired
    EcomService ecomService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<SendoProduct>>> getLazadaProducts(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId") Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<SendoProduct> products = sendoProductService.getProducts(ecomId, offset, limit, sortBy, orderBy);
        return ResponseEntity.ok(GApiResponse.success(products));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/variant")
    public ResponseEntity<GApiResponse<Page<SendoVariant>>> getLazadaVariants(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<SendoVariant> variants = sendoProductService.getVariants(ecomId, offset, limit, sortBy, orderBy);
        return ResponseEntity.ok(GApiResponse.success(variants));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("publishSendoProduct")
    public ResponseEntity<GApiResponse> postSendoProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId,
            @RequestParam(name = "productId", required = false) Long productId
    ) {

        var response = sendoProductService.getPublishCreateSystemProductToSendoPayload(ecomId, productId);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("publishAllSendoProduct")
    public ResponseEntity<GApiResponse> postAllSendoProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId", required = false) Long ecomId
    ) {
        boolean status = sendoProductService.postAllProductToSendo(ecomId);
        return ResponseEntity.ok(GApiResponse.success("Gửi thông tin đăng sản phẩm vào hàng đợi thành công", status));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/fetch")
    public ResponseEntity<GApiResponse> fetchSendoProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "ecomId") Long ecomId
    ) {
        boolean report = sendoProductService.fetchAndSyncSendoProducts(ecomId);
        return ResponseEntity.ok(GApiResponse.success(report));
    }

}
