package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.SaleChannelService;
import com.wifosell.zeus.utils.Preprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/saleChannels")
public class SaleChannelController {
    private final SaleChannelService saleChannelService;

    @Autowired
    public SaleChannelController(SaleChannelService saleChannelService) {
        this.saleChannelService = saleChannelService;
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> getAllSaleChannels(
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<SaleChannel> saleChannels = saleChannelService.getAllSaleChannels(isActive);
        return ResponseEntity.ok(GApiResponse.success(saleChannels));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> getSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<SaleChannel> saleChannels;
        if (shopIds == null) {
            saleChannels = saleChannelService.getSaleChannelsByUserId(userPrincipal.getId(), isActive);
        } else {
            saleChannels = saleChannelService.getSaleChannelsByShopIds(userPrincipal.getId(), shopIds, isActive);
        }
        return ResponseEntity.ok(GApiResponse.success(saleChannels));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}")
    public ResponseEntity<GApiResponse<SaleChannel>> getSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId
    ) {
        SaleChannel saleChannel = saleChannelService.getSaleChannel(userPrincipal.getId(), saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<SaleChannel>> addSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody SaleChannelRequest request
    ) {
        SaleChannel saleChannel = saleChannelService.addSaleChannel(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{saleChannelId}/update")
    public ResponseEntity<GApiResponse<SaleChannel>> addSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId,
            @RequestBody SaleChannelRequest request
    ) {
        SaleChannel saleChannel = saleChannelService.updateSaleChannel(userPrincipal.getId(), saleChannelId, request);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/activate")
    public ResponseEntity<GApiResponse<SaleChannel>> activateSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId
    ) {
        SaleChannel saleChannel = saleChannelService.activateSaleChannel(userPrincipal.getId(), saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/deactivate")
    public ResponseEntity<GApiResponse<SaleChannel>> deactivateSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId
    ) {
        SaleChannel saleChannel = saleChannelService.deactivateSaleChannel(userPrincipal.getId(), saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> activateSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<SaleChannel> saleChannels = saleChannelService.activateSaleChannels(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(saleChannels));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> deactivateSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<SaleChannel> saleChannels = saleChannelService.deactivateSaleChannels(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(saleChannels));
    }
}
