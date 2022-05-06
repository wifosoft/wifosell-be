package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.voucher.Voucher;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.voucher.VoucherRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Voucher>>> getAllVoucher() {
        List<Voucher> voucherList = voucherService.getAllVouchers();
        return ResponseEntity.ok(GApiResponse.success(voucherList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Voucher>>> getVouchers(@CurrentUser UserPrincipal userPrincipal) {
        List<Voucher> voucherList = voucherService.getVouchersByUserId(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(voucherList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/shop={shopId}")
    public ResponseEntity<GApiResponse<List<Voucher>>> getVouchersByShopId(@CurrentUser UserPrincipal userPrincipal,
                                                                           @PathVariable(name = "shopId") Long shopId) {
        List<Voucher> voucherList = voucherService.getVouchersByShopId(shopId);
        return ResponseEntity.ok(GApiResponse.success(voucherList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{voucherId}")
    public ResponseEntity<GApiResponse<Voucher>> getVoucher(@CurrentUser UserPrincipal userPrincipal,
                                                            @PathVariable(name = "voucherId") Long voucherId) {
        Voucher voucher = voucherService.getVoucher(voucherId);
        return ResponseEntity.ok(GApiResponse.success(voucher));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<Voucher>> addVoucher(@CurrentUser UserPrincipal userPrincipal,
                                                            @RequestBody VoucherRequest voucherRequest) {
        Voucher voucher = voucherService.addVoucher(userPrincipal.getId(), voucherRequest);
        return ResponseEntity.ok(GApiResponse.success(voucher));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{voucherId}/update")
    public ResponseEntity<GApiResponse<Voucher>> addVoucher(@CurrentUser UserPrincipal userPrincipal,
                                                            @PathVariable(name = "voucherId") Long voucherId,
                                                            @RequestBody VoucherRequest voucherRequest) {
        Voucher voucher = voucherService.updateVoucher(voucherId, voucherRequest);
        return ResponseEntity.ok(GApiResponse.success(voucher));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{voucherId}/activate")
    public ResponseEntity<GApiResponse<Voucher>> activateVoucher(@CurrentUser UserPrincipal userPrincipal,
                                                                 @PathVariable(name = "voucherId") Long voucherId) {
        Voucher voucher = voucherService.activateVoucher(voucherId);
        return ResponseEntity.ok(GApiResponse.success(voucher));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{voucherId}/deactivate")
    public ResponseEntity<GApiResponse<Voucher>> deactivateVoucher(@CurrentUser UserPrincipal userPrincipal,
                                                                   @PathVariable(name = "voucherId") Long voucherId) {
        Voucher voucher = voucherService.deactivateSaleChannel(voucherId);
        return ResponseEntity.ok(GApiResponse.success(voucher));
    }
}
