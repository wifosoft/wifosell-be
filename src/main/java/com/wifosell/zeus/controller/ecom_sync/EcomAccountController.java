package com.wifosell.zeus.controller.ecom_sync;


import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.EcomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/ecom_sync/ecom_account")
public class EcomAccountController {
    EcomService ecomService;

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GApiResponse<List<EcomAccount>>> getListAccount(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(GApiResponse.success(ecomService.getListEcomAccount(userPrincipal.getId())));
    }
}
