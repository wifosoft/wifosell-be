package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.sale_channel.SaleChannelRequest;
import com.wifosell.zeus.payload.response.sale_channel.SaleChannelResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.SaleChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<GApiResponse<Page<SaleChannelResponse>>> getAllSaleChannels(
            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<SaleChannel> saleChannels = saleChannelService.getSaleChannels(
                null, shopIds, isActives, offset, limit, sortBy, orderBy);
        Page<SaleChannelResponse> responses = saleChannels.map(SaleChannelResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<SaleChannelResponse>>> getSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<SaleChannel> saleChannels = saleChannelService.getSaleChannels(
                userPrincipal.getId(), shopIds, isActives, offset, limit, sortBy, orderBy
        );
        Page<SaleChannelResponse> responses = saleChannels.map(SaleChannelResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}")
    public ResponseEntity<GApiResponse<SaleChannelResponse>> getSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId
    ) {
        SaleChannel saleChannel = saleChannelService.getSaleChannel(userPrincipal.getId(), saleChannelId);
        SaleChannelResponse response = new SaleChannelResponse(saleChannel);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<SaleChannelResponse>> addSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid SaleChannelRequest request
    ) {
        SaleChannel saleChannel = saleChannelService.addSaleChannel(userPrincipal.getId(), request);
        SaleChannelResponse response = new SaleChannelResponse(saleChannel);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{saleChannelId}/update")
    public ResponseEntity<GApiResponse<SaleChannelResponse>> addSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId,
            @RequestBody @Valid SaleChannelRequest request
    ) {
        SaleChannel saleChannel = saleChannelService.updateSaleChannel(userPrincipal.getId(), saleChannelId, request);
        SaleChannelResponse response = new SaleChannelResponse(saleChannel);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/activate")
    public ResponseEntity<GApiResponse<SaleChannelResponse>> activateSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId
    ) {
        SaleChannel saleChannel = saleChannelService.activateSaleChannel(userPrincipal.getId(), saleChannelId);
        SaleChannelResponse response = new SaleChannelResponse(saleChannel);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{saleChannelId}/deactivate")
    public ResponseEntity<GApiResponse<SaleChannelResponse>> deactivateSaleChannel(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "saleChannelId") Long saleChannelId
    ) {
        SaleChannel saleChannel = saleChannelService.deactivateSaleChannel(userPrincipal.getId(), saleChannelId);
        SaleChannelResponse response = new SaleChannelResponse(saleChannel);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<SaleChannelResponse>>> activateSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request) {
        List<SaleChannel> saleChannels = saleChannelService.activateSaleChannels(userPrincipal.getId(), request.getIds());
        List<SaleChannelResponse> responses = saleChannels.stream().map(SaleChannelResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<SaleChannelResponse>>> deactivateSaleChannels(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request) {
        List<SaleChannel> saleChannels = saleChannelService.deactivateSaleChannels(userPrincipal.getId(), request.getIds());
        List<SaleChannelResponse> responses = saleChannels.stream().map(SaleChannelResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
