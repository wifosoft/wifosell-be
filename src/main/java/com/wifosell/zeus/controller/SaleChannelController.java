package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.SaleChannelService;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> getAllSaleChannels() {
        List<SaleChannel> saleChannelList = saleChannelService.getAllSaleChannels();
        return ResponseEntity.ok(GApiResponse.success(saleChannelList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> getSaleChannels(@CurrentUser UserPrincipal userPrincipal) {
        List<SaleChannel> saleChannelList = saleChannelService.getSaleChannelsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(saleChannelList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/shop={shopId}")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> getSaleChannelsByShopId(@CurrentUser UserPrincipal userPrincipal,
                                                                           @PathVariable(name = "shopId") Long shopId) {
        List<SaleChannel> saleChannelList = saleChannelService.getSaleChannelsByShopId(shopId);
        return ResponseEntity.ok(GApiResponse.success(saleChannelList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}")
    public ResponseEntity<GApiResponse<SaleChannel>> getSaleChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                    @PathVariable(name = "saleChannelId") Long saleChannelId) {
        SaleChannel saleChannel = saleChannelService.getSaleChannel(saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<SaleChannel>> addSaleChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                    @RequestBody SaleChannelRequest saleChannelRequest) {
        SaleChannel saleChannel = saleChannelService.addSaleChannel(userPrincipal.getId(), saleChannelRequest);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{saleChannelId}/update")
    public ResponseEntity<GApiResponse<SaleChannel>> addSaleChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                    @PathVariable(name = "saleChannelId") Long saleChannelId,
                                                                    @RequestBody SaleChannelRequest saleChannelRequest) {
        SaleChannel saleChannel = saleChannelService.updateSaleChannel(saleChannelId, saleChannelRequest);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/activate")
    public ResponseEntity<GApiResponse<SaleChannel>> activateSaleChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                         @PathVariable(name = "saleChannelId") Long saleChannelId) {
        SaleChannel saleChannel = saleChannelService.activateSaleChannel(saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/deactivate")
    public ResponseEntity<GApiResponse<SaleChannel>> deactivateSaleChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                           @PathVariable(name = "saleChannelId") Long saleChannelId) {
        SaleChannel saleChannel = saleChannelService.deactivateSaleChannel(saleChannelId);
        return ResponseEntity.ok(GApiResponse.success(saleChannel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> activateSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<SaleChannel> saleChannels = saleChannelService.activateSaleChannels(request.getIds());
        return ResponseEntity.ok(GApiResponse.success(saleChannels));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<SaleChannel>>> deactivateSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<SaleChannel> saleChannels = saleChannelService.deactivateSaleChannels(request.getIds());
        return ResponseEntity.ok(GApiResponse.success(saleChannels));
    }
}
