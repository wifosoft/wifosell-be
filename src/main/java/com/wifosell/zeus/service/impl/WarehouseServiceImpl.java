package com.wifosell.zeus.service.impl;

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
import java.util.stream.Collectors;


@Transactional
@Service("WarehouseService")
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                UserRepository userRepository) {
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Warehouse> getAllWarehouse() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse addWarehouse(Long userId, WarehouseRequest warehouseRequest) {
        User gm = userRepository.getUserById(userId);
        Warehouse warehouse = new Warehouse();
        this.updateWarehouseByRequest(warehouse, warehouseRequest);
        warehouse.setGeneralManager(gm);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse getWarehouse(Long warehouseId) {
        return warehouseRepository.getWarehouseById(warehouseId);
    }

    @Override
    public Warehouse updateWarehouse(Long warehouseId, WarehouseRequest warehouseRequest) {
        Warehouse warehouse = warehouseRepository.getWarehouseById(warehouseId);
        this.updateWarehouseByRequest(warehouse, warehouseRequest);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse activateWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.getWarehouseById( warehouseId);
        warehouse.setIsActive(true);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse deActivateWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.getWarehouseById( warehouseId);
        warehouse.setIsActive(false);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> activateWarehouses(List<Long> warehouseIds) {
        return warehouseIds.stream().map(this::activateWarehouse).collect(Collectors.toList());
    }

    @Override
    public List<Warehouse> deactivateWarehouses(List<Long> warehouseIds) {
        return warehouseIds.stream().map(this::deActivateWarehouse).collect(Collectors.toList());
    }

    private void updateWarehouseByRequest(Warehouse warehouse, WarehouseRequest warehouseRequest) {
        Optional.ofNullable(warehouseRequest.getName()).ifPresent(warehouse::setName);
        Optional.ofNullable(warehouseRequest.getShortName()).ifPresent(warehouse::setShortName);
        Optional.ofNullable(warehouseRequest.getAddress()).ifPresent(warehouse::setAddress);
        Optional.ofNullable(warehouseRequest.getPhone()).ifPresent(warehouse::setPhone);
        Optional.ofNullable(warehouseRequest.getDescription()).ifPresent(warehouse::setDescription);
        Optional.ofNullable(warehouseRequest.getActive()).ifPresent(warehouse::setIsActive);
    }
}
