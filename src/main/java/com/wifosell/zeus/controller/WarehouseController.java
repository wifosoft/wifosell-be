package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.payload.response.warehouse.WarehouseResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<GApiResponse<Page<WarehouseResponse>>> getAllWarehouses(
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<Warehouse> warehouses = warehouseService.getWarehouses(null, isActives, offset, limit, sortBy, orderBy);
        Page<WarehouseResponse> responses = warehouses.map(WarehouseResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<WarehouseResponse>>> getWarehouses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<Warehouse> warehouses = warehouseService.getWarehouses(userPrincipal.getId(), isActives, offset, limit, sortBy, orderBy);
        Page<WarehouseResponse> responses = warehouses.map(WarehouseResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{warehouseId}")
    public ResponseEntity<GApiResponse<WarehouseResponse>> getWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.getWarehouse(userPrincipal.getId(), warehouseId);
        WarehouseResponse response = new WarehouseResponse(warehouse);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<WarehouseResponse>> addWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody WarehouseRequest request
    ) {
        Warehouse warehouse = warehouseService.addWarehouse(userPrincipal.getId(), request);
        WarehouseResponse response = new WarehouseResponse(warehouse);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{warehouseId}/update")
    public ResponseEntity<GApiResponse<WarehouseResponse>> updateWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId,
            @RequestBody WarehouseRequest request
    ) {
        Warehouse warehouse = warehouseService.updateWarehouse(userPrincipal.getId(), warehouseId, request);
        WarehouseResponse response = new WarehouseResponse(warehouse);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{warehouseId}/activate")
    public ResponseEntity<GApiResponse<WarehouseResponse>> activateWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.activateWarehouse(userPrincipal.getId(), warehouseId);
        WarehouseResponse response = new WarehouseResponse(warehouse);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{warehouseId}/deActivate")
    public ResponseEntity<GApiResponse<WarehouseResponse>> deactivateWarehouse(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "warehouseId") Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.deactivateWarehouse(userPrincipal.getId(), warehouseId);
        WarehouseResponse response = new WarehouseResponse(warehouse);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<WarehouseResponse>>> activateWarehouses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<Warehouse> warehouses = warehouseService.activateWarehouses(userPrincipal.getId(), request.getIds());
        List<WarehouseResponse> responses = warehouses.stream().map(WarehouseResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<WarehouseResponse>>> deactivateWarehouses(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request) {
        List<Warehouse> warehouses = warehouseService.deactivateWarehouses(userPrincipal.getId(), request.getIds());
        List<WarehouseResponse> responses = warehouses.stream().map(WarehouseResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
