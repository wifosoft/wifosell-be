package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.repository.WarehouseRepository;
import com.wifosell.zeus.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("WarehouseService")
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> getAllWarehouse() {
        List<Warehouse> lsWarehouse = warehouseRepository.findAll();
        return lsWarehouse;
    }
}
