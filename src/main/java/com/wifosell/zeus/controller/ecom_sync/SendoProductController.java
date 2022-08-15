package com.wifosell.zeus.controller.ecom_sync;

import com.google.gson.Gson;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTOWithModel;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.SendoProductService;
import com.wifosell.zeus.taurus.core.TaurusBus;
import com.wifosell.zeus.taurus.core.payload.KafkaPublishProductSendoPayload;
import com.wifosell.zeus.taurus.core.payload.TaurusKafkaPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.GenericSignatureFormatError;

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
        SendoLinkAccountRequestDTOWithModel sendoInfo = ecomService.getSendoDTO(ecomId);

        var response = sendoProductService.pulishCreateSystemProductToSendo(ecomId, productId);

        KafkaPublishProductSendoPayload kafkaPublishProductSendoPayload = new KafkaPublishProductSendoPayload();
        kafkaPublishProductSendoPayload.setShop_key(sendoInfo.getShop_key());
        kafkaPublishProductSendoPayload.setSecret_key(sendoInfo.getSecret_key());
        kafkaPublishProductSendoPayload.setPublish_data_json(response);
        var payloadStr = TaurusBus.buildPayloadMessageString(kafkaPublishProductSendoPayload , "sendo.product.publish");
        //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));
        kafkaTemplate.send("publish_sendo_product", payloadStr );
        return ResponseEntity.ok(GApiResponse.success(response));
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
