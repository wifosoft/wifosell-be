package com.wifosell.zeus.controller.ecom_sync;


import com.google.gson.Gson;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.constant.LazadaEcomSyncConst;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.provider.lazada.ResponseTokenPayload;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.service.UserService;
import com.wifosell.zeus.taurus.lazada.LazadaClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("api/ecom_sync/ecom_account")
public class EcomAccountController {
    @Autowired
    EcomService ecomService;

    @Autowired
    UserService userService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GApiResponse<List<EcomAccount>>> getListAccount(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(GApiResponse.success(ecomService.getListEcomAccount(userPrincipal.getId())));
    }

    //https://wifosell-dev.com:8888/api/ecom_sync/ecom_account/callback_lazada?userId=1&code=0_117995_fYPaTx1hvwI2M1PnVbSCNjIV92947
    @GetMapping("/get_url_callback_lazada")
    public ResponseEntity<GApiResponse> getUrlCallbackLazada(@CurrentUser UserPrincipal userPrincipal) {
        String customSign = DigestUtils
                .md5Hex(userPrincipal.getUsername() + userPrincipal.getId());

        EcomAccountLazadaCallbackPayload callbackPayloadRequest =
                EcomAccountLazadaCallbackPayload.builder()
                        .userId(userPrincipal.getId())
                        .signature(customSign)
                        .feCallback(LazadaEcomSyncConst.FE_RETURN_URL)
                        .build();
        String jsonCallbackQuery = (new Gson()).toJson(callbackPayloadRequest);
        String base64_query = Base64.getEncoder().encodeToString(jsonCallbackQuery.getBytes());
        String query_callback = String.format(LazadaEcomSyncConst.FORMAT_CALLBACK, base64_query);
        return ResponseEntity.ok(GApiResponse.success(query_callback));
    }

    @GetMapping("/callback_lazada")
    public ResponseEntity<GApiResponse> callbackLazadaAccount(
            @RequestParam("code") String code,
            @RequestParam("data") String data
    ) throws ApiException {
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        String decodedString = new String(decodedBytes);
        EcomAccountLazadaCallbackPayload payload = (new Gson()).fromJson(decodedString, EcomAccountLazadaCallbackPayload.class);
        payload.setCode(code);

        Long userID = payload.getUserId();

        User user =userService.getUserInfo(userID);
        String customSign = DigestUtils
                .md5Hex(user.getUsername() + user.getId());
        if(!customSign.equals(payload.getSignature())){
            throw new ZeusGlobalException(HttpStatus.OK, "Signature không đúng");
        }

        LazopRequest request = new LazopRequest("/auth/token/create");
        request.addApiParameter("code", payload.getCode());
        ResponseTokenPayload responseTokenPayload = LazadaClient.executeMappingModel(request, ResponseTokenPayload.class);

        if(responseTokenPayload.getAccess_token() == null ){
            throw new ZeusGlobalException(HttpStatus.OK, "Vui lòng đăng nhập lại thông qua lazada");
        }
        payload.setTokenAuthResponse(responseTokenPayload);

        EcomAccount ecomAccount  = ecomService.addEcomAccountLazadaFromCallback(payload);
        return ResponseEntity.ok(GApiResponse.success(ecomAccount));
    }
}
