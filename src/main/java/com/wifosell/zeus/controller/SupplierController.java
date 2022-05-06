package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.payload.request.supplier.UpdateSupplierRequest;
import com.wifosell.zeus.payload.response.supplier.SupplierResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.SupplierService;
import com.wifosell.zeus.utils.Preprocessor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/suppliers")
@AllArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<SupplierResponse>>> getSuppliers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "active", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Supplier> suppliers = supplierService.getSuppliers(userPrincipal.getId(), isActive);
        List<SupplierResponse> responses = suppliers.stream().map(SupplierResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{supplierId}")
    public ResponseEntity<GApiResponse<SupplierResponse>> getSupplier(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "supplierId") Long supplierId
    ) {
        Supplier supplier = supplierService.getSupplier(userPrincipal.getId(), supplierId);
        SupplierResponse response = new SupplierResponse(supplier);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<SupplierResponse>> addSupplier(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody AddSupplierRequest request
    ) {
        Supplier supplier = supplierService.addSupplier(userPrincipal.getId(), request);
        SupplierResponse response = new SupplierResponse(supplier);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{supplierId}/update")
    public ResponseEntity<GApiResponse<SupplierResponse>> updateSupplier(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "supplierId") Long supplierId,
            @RequestBody UpdateSupplierRequest request
    ) {
        Supplier supplier = supplierService.updateSupplier(userPrincipal.getId(), supplierId, request);
        SupplierResponse response = new SupplierResponse(supplier);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{supplierId}/activate")
    public ResponseEntity<GApiResponse<SupplierResponse>> activateSupplier(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "supplierId") Long supplierId
    ) {
        Supplier supplier = supplierService.activateSupplier(userPrincipal.getId(), supplierId);
        SupplierResponse response = new SupplierResponse(supplier);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{supplierId}/deactivate")
    public ResponseEntity<GApiResponse<SupplierResponse>> deactivateSupplier(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "supplierId") Long supplierId
    ) {
        Supplier supplier = supplierService.deactivateSupplier(userPrincipal.getId(), supplierId);
        SupplierResponse response = new SupplierResponse(supplier);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<SupplierResponse>>> activateSuppliers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Supplier> suppliers = supplierService.activateSuppliers(userPrincipal.getId(), request.getIds());
        List<SupplierResponse> responses = suppliers.stream().map(SupplierResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated() and hasRole('GENERAL_MANAGER')")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<SupplierResponse>>> deactivateSuppliers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Supplier> suppliers = supplierService.deactivateSuppliers(userPrincipal.getId(), request.getIds());
        List<SupplierResponse> responses = suppliers.stream().map(SupplierResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
