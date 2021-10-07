package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.WarehouseRepository;
import com.wifosell.zeus.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Service("WarehouseService")
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Warehouse> getAllWarehouse() {
        List<Warehouse> lsWarehouse = warehouseRepository.findAll();
        return lsWarehouse;
    }

    @Override
    public Warehouse addWarehouse(Long userId, WarehouseRequest warehouseRequest) {
        User gm = userRepository.getUserById(userId);
        Warehouse warehouse = new Warehouse();
        Optional.ofNullable(warehouseRequest.getName()).ifPresent(warehouse::setName);
        Optional.ofNullable(warehouseRequest.getShortName()).ifPresent(warehouse::setShortName);
        Optional.ofNullable(warehouseRequest.getAddress()).ifPresent(warehouse::setAddress);
        Optional.ofNullable(warehouseRequest.getPhone()).ifPresent(warehouse::setPhone);
        Optional.ofNullable(warehouseRequest.getDescription()).ifPresent(warehouse::setDescription);
        warehouse.setGeneralManager(gm);
        warehouse = warehouseRepository.save(warehouse);
        return warehouse;
    }

    @Override
    public Warehouse getWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.getWarehouseById(warehouseId);
        return warehouse;
    }

    @Override
    public Warehouse updateWarehouse(Long warehouseId, WarehouseRequest warehouseRequest) {
        Warehouse warehouse = warehouseRepository.getWarehouseById(warehouseId);
        Optional.ofNullable(warehouseRequest.getName()).ifPresent(warehouse::setName);
        Optional.ofNullable(warehouseRequest.getShortName()).ifPresent(warehouse::setShortName);
        Optional.ofNullable(warehouseRequest.getAddress()).ifPresent(warehouse::setAddress);
        Optional.ofNullable(warehouseRequest.getPhone()).ifPresent(warehouse::setPhone);
        Optional.ofNullable(warehouseRequest.getDescription()).ifPresent(warehouse::setDescription);
        warehouse = warehouseRepository.save(warehouse);
        return warehouse;
    }

}
