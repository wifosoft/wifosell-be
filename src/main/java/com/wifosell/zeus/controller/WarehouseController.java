package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.WarehouseService;
import com.wifosell.zeus.utils.Preprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Warehouse>>> getAllWarehouses(
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Warehouse> warehouses = warehouseService.getAllWarehouses(isActive);
        return ResponseEntity.ok(GApiResponse.success(warehouses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Warehouse>>> getWarehouses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Warehouse> warehouses = warehouseService.getWarehouses(userPrincipal.getId(), isActive);
        return ResponseEntity.ok(GApiResponse.success(warehouses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{warehouseId}")
    public ResponseEntity<GApiResponse<Warehouse>> getWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.getWarehouse(userPrincipal.getId(), warehouseId);
        return ResponseEntity.ok(GApiResponse.success(warehouse));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<Warehouse>> addWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody WarehouseRequest request
    ) {
        Warehouse warehouse = warehouseService.addWarehouse(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(warehouse));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{warehouseId}/update")
    public ResponseEntity<GApiResponse<Warehouse>> updateWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId,
            @RequestBody WarehouseRequest request
    ) {
        Warehouse warehouse = warehouseService.updateWarehouse(userPrincipal.getId(), warehouseId, request);
        return ResponseEntity.ok(GApiResponse.success(warehouse));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{warehouseId}/activate")
    public ResponseEntity<GApiResponse<Warehouse>> activateWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.activateWarehouse(userPrincipal.getId(), warehouseId);
        return ResponseEntity.ok(GApiResponse.success(warehouse));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{warehouseId}/deActivate")
    public ResponseEntity<GApiResponse<Warehouse>> deactiveWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.deactivateWarehouse(userPrincipal.getId(), warehouseId);
        return ResponseEntity.ok(GApiResponse.success(warehouse));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<Warehouse>>> activateWarehouses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<Warehouse> warehouses = warehouseService.activateWarehouses(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(warehouses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<Warehouse>>> deactivateWarehouses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<Warehouse> warehouses = warehouseService.deactivateWarehouses(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(warehouses));
    }
}
