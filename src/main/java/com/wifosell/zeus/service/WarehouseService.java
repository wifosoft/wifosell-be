package com.wifosell.zeus.service;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import lombok.NonNull;

import java.util.List;


public interface WarehouseService {
    List<Warehouse> getAllWarehouses(Boolean isActive);

    List<Warehouse> getWarehouses(@NonNull Long userId, Boolean isActive);

    Warehouse getWarehouse(@NonNull Long userId, @NonNull Long warehouseId);

    Warehouse addWarehouse(@NonNull Long userId, @NonNull WarehouseRequest warehouseRequest);

    Warehouse updateWarehouse(@NonNull Long userId, @NonNull Long warehouseId, @NonNull WarehouseRequest warehouseRequest);

    Warehouse activateWarehouse(@NonNull Long userId, @NonNull Long warehouseId);

    Warehouse deactivateWarehouse(@NonNull Long userId, @NonNull Long warehouseId);

    List<Warehouse> activateWarehouses(@NonNull Long userId, @NonNull List<Long> warehouseIds);

    List<Warehouse> deactivateWarehouses(@NonNull Long userId, @NonNull List<Long> warehouseIds);
}
