package com.wifosell.zeus.service;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;


public interface WarehouseService {
    Page<Warehouse> getWarehouses(Long userId, List<Boolean> isActives,
                                  Integer offset, Integer limit, String sortBy, String orderBy);

    List<Warehouse> getWarehousesByShopIdsAndSaleChannelIds(List<Long> shopIds, List<Long> saleChannelIds);

    Warehouse getWarehouse(Long userId, @NonNull Long warehouseId);

    Warehouse addWarehouse(Long userId, @NonNull WarehouseRequest warehouseRequest);

    Warehouse updateWarehouse(Long userId, @NonNull Long warehouseId, @NonNull WarehouseRequest warehouseRequest);

    Warehouse activateWarehouse(Long userId, @NonNull Long warehouseId);

    Warehouse deactivateWarehouse(Long userId, @NonNull Long warehouseId);

    List<Warehouse> activateWarehouses(Long userId, @NonNull List<Long> warehouseIds);

    List<Warehouse> deactivateWarehouses(Long userId, @NonNull List<Long> warehouseIds);
}
