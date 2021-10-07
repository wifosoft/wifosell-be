package com.wifosell.zeus.service;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;

import java.util.List;


public interface WarehouseService {
    List<Warehouse> getAllWarehouse();

    Warehouse addWarehouse(Long userId, WarehouseRequest warehouseRequest);
    Warehouse getWarehouse(Long warehouseId);
    Warehouse updateWarehouse(Long warehouseId, WarehouseRequest warehouseRequest);
    Warehouse activateWarehouse(Long warehouseId);
    Warehouse deActivateWarehouse(Long warehouseId);
    //Lấy toàn bộ warehouse trong chuỗi cửa hàng


}
