package com.wifosell.zeus.controller.ecom_sync;


import com.google.gson.Gson;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.config.property.AppProperties;
import com.wifosell.zeus.constant.LazadaEcomSyncConst;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.provider.lazada.ResponseSellerInfoPayload;
import com.wifosell.zeus.payload.provider.lazada.ResponseTokenPayload;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import com.wifosell.zeus.payload.request.ecom_sync.PostQueryUrlCallbackLazadaRequest;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.UserService;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import com.wifosell.zeus.utils.ConvertorType;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("api/ecom_sync/ecom_account")
public class EcomAccountController {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    AppProperties appProperties;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EcomService ecomService;

    @Autowired
    UserService userService;

    @GetMapping("enum_const")
    public ResponseEntity<GApiResponse> getEnumConst() {
        @Getter
        @Setter
        class EnumTypeConst {
            public String[] ecomName = ConvertorType.getNames(EcomAccount.EcomName.class);
            public String[] accountStatus = ConvertorType.getNames(EcomAccount.AccountStatus.class);
        }
        return ResponseEntity.ok(GApiResponse.success(new EnumTypeConst()));
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GApiResponse<List<EcomAccount>>> getListAccount(@CurrentUser UserPrincipal userPrincipal) {

        return ResponseEntity.ok(GApiResponse.success(ecomService.getListEcomAccount(userPrincipal.getId())));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}/delete")
    public ResponseEntity<GApiResponse> deleteEcomAccount(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "id") Long ecomAccountId) {
        boolean ck = ecomService.deleteEcomAccount(ecomAccountId);
        return ResponseEntity.ok(GApiResponse.success(ck));
    }


    //https://wifosell-dev.com:8888/api/ecom_sync/ecom_account/lazada/callback?userId=1&code=0_117995_fYPaTx1hvwI2M1PnVbSCNjIV92947
    @GetMapping("/lazada/get_url_callback")
    public ResponseEntity<GApiResponse> getUrlCallbackLazada(@CurrentUser UserPrincipal userPrincipal) {
        String customSign = DigestUtils
                .md5Hex(userPrincipal.getUsername() + userPrincipal.getId());

        EcomAccountLazadaCallbackPayload callbackPayloadRequest =
                EcomAccountLazadaCallbackPayload.builder()
                        .userId(userPrincipal.getId())
                        .signature(customSign)
                        .feCallbackDomain(LazadaEcomSyncConst.FE_BASE_DOMAIN)
                        .feCallbackUrl("")
                        .build();
        String jsonCallbackQuery = (new Gson()).toJson(callbackPayloadRequest);
        String base64_query = Base64.getEncoder().encodeToString(jsonCallbackQuery.getBytes());
        String query_callback = String.format(LazadaEcomSyncConst.FORMAT_CALLBACK, LazadaEcomSyncConst.FE_BASE_DOMAIN, base64_query);
        return ResponseEntity.ok(GApiResponse.success(query_callback));
    }

    @PostMapping("/lazada/get_url_callback")
    public ResponseEntity<GApiResponse> postGetUrlCallbackLazada(@CurrentUser UserPrincipal userPrincipal, @RequestBody PostQueryUrlCallbackLazadaRequest body) {
        String customSign = DigestUtils
                .md5Hex(userPrincipal.getUsername() + userPrincipal.getId());

        EcomAccountLazadaCallbackPayload callbackPayloadRequest =
                EcomAccountLazadaCallbackPayload.builder()
                        .userId(userPrincipal.getId())
                        .signature(customSign)
                        .feCallbackDomain(body.getFeCallbackDomain())
                        .feCallbackUrl(body.getFeCallbackUrl())
                        .build();
        String jsonCallbackQuery = (new Gson()).toJson(callbackPayloadRequest);
        String base64_query = Base64.getEncoder().encodeToString(jsonCallbackQuery.getBytes());
        String query_callback = String.format(LazadaEcomSyncConst.FORMAT_CALLBACK, body.getFeCallbackDomain(), base64_query);
        return ResponseEntity.ok(GApiResponse.success(query_callback));
    }


    @GetMapping("/lazada/callback")
    public ResponseEntity<GApiResponse> callbackLazadaAccount(
            @RequestParam("code") String code,
            @RequestParam("data") String data
    ) throws ApiException, UnsupportedEncodingException {
        String dataDecode = java.net.URLDecoder.decode(data, StandardCharsets.UTF_8.name());

        byte[] decodedBytes = Base64.getDecoder().decode(dataDecode);
        String decodedString = new String(decodedBytes);
        EcomAccountLazadaCallbackPayload payload = (new Gson()).fromJson(decodedString, EcomAccountLazadaCallbackPayload.class);
        payload.setCode(code);

        Long userID = payload.getUserId();

        User user = userService.getUserInfo(userID);
        String customSign = DigestUtils
                .md5Hex(user.getUsername() + user.getId());
        if (!customSign.equals(payload.getSignature())) {
            throw new ZeusGlobalException(HttpStatus.OK, "Signature không đúng");
        }

        LazopRequest request = new LazopRequest("/auth/token/create");
        request.addApiParameter("code", payload.getCode());
        ResponseTokenPayload responseTokenPayload = LazadaClient.executeMappingModelWithClient(LazadaClient.getClientAuth(), request, ResponseTokenPayload.class);

        if (responseTokenPayload.getAccess_token() == null) {
            throw new ZeusGlobalException(HttpStatus.OK, "Vui lòng đăng nhập lại thông qua lazada");
        }
        payload.setTokenAuthResponse(responseTokenPayload);

        EcomAccount ecomAccount = ecomService.addEcomAccountLazadaFromCallback(payload);
        if (!payload.getFeCallbackUrl().isEmpty()) {
            //redirect về url đã post
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(payload.getFeCallbackUrl())).build();
        }
        return ResponseEntity.ok(GApiResponse.success(ecomAccount));
    }


    @GetMapping("/lazada/{ecomId}/getAccountInfo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GApiResponse> getLazadaAccountInfo(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "ecomId") Long ecomId
    ) throws ApiException {
        EcomAccount ecomAccount = ecomService.getEcomAccount(ecomId);
        LazopRequest request = new LazopRequest();
        request.setApiName("/seller/get");
        request.setHttpMethod("GET");
        ResponseSellerInfoPayload responseTokenPayload = LazadaClient.executeMappingModel(request, ResponseSellerInfoPayload.class, ecomAccount.getAccessToken());

        return ResponseEntity.ok(GApiResponse.success(responseTokenPayload));
    }

    @GetMapping("/lazada/{ecomId}/getProductsFromEcommerce")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GApiResponse> getProductsFromEcommerce(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "ecomId") Long ecomId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "50") int limit
    ) throws ApiException {
        return ResponseEntity.ok(GApiResponse.success(ecomService.getProductsFromEcommerce(ecomId, offset, limit)));
    }

    @GetMapping("/lazada/{ecomId}/getAllProductsFromEcommerce")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GApiResponse> getAllProductsFromEcommerce(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "ecomId") Long ecomId
    ) throws ApiException {
        return ResponseEntity.ok(GApiResponse.success(ecomService.getAllProductsFromEcommerce(ecomId, 50)));
    }

    @GetMapping("/lazada/crawlCategoryTree")
    public ResponseEntity<GApiResponse> crawlCategoryTree() throws ApiException {
        ecomService.crawlCategoryTree();
        return ResponseEntity.ok(GApiResponse.success(""));
    }

    @GetMapping("/lazada/crawlCategoryAtrribute")
    public ResponseEntity<GApiResponse> crawlCategoryAttribute() throws ApiException {
        ecomService.crawlCategoryAttribute();
        return ResponseEntity.ok(GApiResponse.success("KO"));
    }

    @GetMapping("/lazada/getCategoryAttribute")
    public ResponseEntity<GApiResponse> getLazadaCategoryAttribute() {
        List<LazadaCategoryAttribute> lazadaCategoryAttributeList = ecomService.getListCategoryAttribute();
        return ResponseEntity.ok(GApiResponse.success(lazadaCategoryAttributeList));
    }


    @GetMapping("/linkWithSaleChannelShopWarehouse")
    public ResponseEntity<GApiResponse> linkEcomAccountSaleChannelShopWarehouse(
            @RequestParam("ecomId") Long ecomId,
            @RequestParam("shopId") Long shopId,
            @RequestParam("warehouseId") Long warehouseId,
            @RequestParam("saleChannelId") Long saleChannelId
    ) {
        LazadaSwwAndEcomAccount link = ecomService.linkEcomAccountToSSW(ecomId, saleChannelId, shopId, warehouseId);
        return ResponseEntity.ok(GApiResponse.success("Thành công", link));
    }


    //liên kết danh mục hàng category và system
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lazada/linkLazadaCategoryAndSysCategory")
    public ResponseEntity<GApiResponse> linkLazadaCategoryAndSysCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("lazadaCategoryId") Long lazadaCategoryId,
            @RequestParam("sysCategoryId") Long sysCategoryId
    ) {
        User user = userRepository.getUser(userPrincipal);
        LazadaCategoryAndSysCategory lazadaCategoryAndSysCategory = ecomService.linkLazadaCategoryAndSysCategory(user, lazadaCategoryId, sysCategoryId);
        return ResponseEntity.ok(GApiResponse.success("Liên kết thành công", lazadaCategoryAndSysCategory));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lazada/getLinkedLazadaCategoryAndSysCategory")
    public ResponseEntity<GApiResponse> getLinkedLazadaCategoryAndSystemCategory(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.getUser(userPrincipal);
        List<LazadaCategoryAndSysCategory> listLinked = ecomService.getLinkedLazadaCategoryAndSysCategory(user);
        return ResponseEntity.ok(GApiResponse.success("Thành công", listLinked));
    }


    //liên kết danh mục hàng variatn và system
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lazada/linkLazadaVariantAndSysVariant")
    public ResponseEntity<GApiResponse> linkLazadaVariantAndSysVariant(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("lazadaVariantId") Long lazadaVariantId,
            @RequestParam("sysVariantId") Long sysVariantId
    ) {
        User user = userRepository.getUser(userPrincipal);
        LazadaVariantAndSysVariant lazadaVariantAndSysVariant = ecomService.linkLazadaVariantAndSysVariant(user, lazadaVariantId, sysVariantId);
        return ResponseEntity.ok(GApiResponse.success("Liên kết thành công", lazadaVariantAndSysVariant));
    }

    @GetMapping("/lazada/getListCategory")
    public ResponseEntity<GApiResponse> getListCategory(
            @RequestParam("isLeaf") boolean isLeaf
    ) {
        List<LazadaCategory> list = ecomService.getListCategory(isLeaf);
        return ResponseEntity.ok(GApiResponse.success(list));
    }

    //Sendo controller
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sendo/linkAccount")
    public ResponseEntity<GApiResponse> postLinkSendoAccount(@CurrentUser UserPrincipal userPrincipal, @RequestBody SendoLinkAccountRequest body) throws ApiException {
        User user = userRepository.getUser(userPrincipal);
        EcomAccount ecomAccount = ecomService.addEcomAccountSendo(user.getId(), body.getShopKey(), body.getSecretKey(), body.getShopName());
        return ResponseEntity.ok(GApiResponse.success(ecomAccount));
    }


}
