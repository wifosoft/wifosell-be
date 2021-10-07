package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    WarehouseService warehouseService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<GApiResponse> getAllWarehouse() {
        return ResponseEntity.ok(GApiResponse.success(warehouseService.getAllWarehouse()));
    }
    


}
