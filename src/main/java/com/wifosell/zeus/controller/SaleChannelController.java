package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.salechannel.SaleChannel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.salechannel.SaleChannelRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.SaleChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salechannels")
public class SaleChannelController {

    @Autowired
    SaleChannelService saleChannelService;

    @GetMapping("")
    public ResponseEntity<GApiResponse> getAllSaleChannel() {
        return ResponseEntity.ok(GApiResponse.success(saleChannelService.getAllSaleChannel()));
    }

    @PostMapping("")
    public ResponseEntity<GApiResponse> addSaleChannel(@CurrentUser UserPrincipal userPrincipal, @RequestBody SaleChannelRequest saleChannelRequest) {
        SaleChannel saleChannel = saleChannelService.addSaleChannel(userPrincipal.getId(), saleChannelRequest);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}")
    public ResponseEntity<GApiResponse> getSaleChannel(@CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "saleChannelId") Long saleChannelId) {

        return ResponseEntity.ok(GApiResponse.success(saleChannelService.getSaleChannel(saleChannelId)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{saleChannelId}/update")
    public ResponseEntity<GApiResponse> updateSaleChannel(@CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "saleChannelId") Long saleChannelId, @RequestBody SaleChannelRequest saleChannelRequest) {
        return ResponseEntity.ok(GApiResponse.success(saleChannelService.updateSaleChannel(saleChannelId, saleChannelRequest)));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/activate")
    public ResponseEntity<GApiResponse> activateSaleChannel(@CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "saleChannelId") Long saleChannelId) {
        return ResponseEntity.ok(GApiResponse.success(saleChannelService.activateSaleChannel(saleChannelId)));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/deActivate")
    public ResponseEntity<GApiResponse> deActiveSaleChannel(@CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "saleChannelId") Long saleChannelId) {
        return ResponseEntity.ok(GApiResponse.success(saleChannelService.deActivateSaleChannel(saleChannelId)));
    }

    //


}
