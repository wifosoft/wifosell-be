package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.WarehouseService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional
@Service("WarehouseService")
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;

    @Override
    public List<Warehouse> getAllWarehouses(Boolean isActive) {
        if (isActive == null)
            return warehouseRepository.findAll();
        return warehouseRepository.findAllWithActive(isActive);
    }

    @Override
    public List<Warehouse> getWarehouses(@NonNull Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return warehouseRepository.findAllWithGm(gm.getId());
        return warehouseRepository.findAllWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public Warehouse getWarehouse(@NonNull Long userId, @NonNull Long warehouseId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return warehouseRepository.getByIdWithGm(gm.getId(), warehouseId);
    }

    @Override
    public Warehouse addWarehouse(@NonNull Long userId, @NonNull WarehouseRequest request) {
        User gm = userRepository.getUserById(userId);
        Warehouse warehouse = new Warehouse();
        return this.updateWarehouseByRequest(warehouse, request, gm);
    }

    @Override
    public Warehouse updateWarehouse(@NonNull Long userId, @NonNull Long warehouseId, @NonNull WarehouseRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseRepository.getByIdWithGm(gm.getId(), warehouseId);
        this.updateWarehouseByRequest(warehouse, request, gm);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse activateWarehouse(@NonNull Long userId, @NonNull Long warehouseId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseRepository.getByIdWithGm(gm.getId(), warehouseId);
        warehouse.setIsActive(true);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse deactivateWarehouse(@NonNull Long userId, @NonNull Long warehouseId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseRepository.getByIdWithGm(gm.getId(), warehouseId);
        warehouse.setIsActive(false);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> activateWarehouses(@NonNull Long userId, @NonNull List<Long> warehouseIds) {
        return warehouseIds.stream().map(id -> this.activateWarehouse(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Warehouse> deactivateWarehouses(@NonNull Long userId, @NonNull List<Long> warehouseIds) {
        return warehouseIds.stream().map(id -> this.deactivateWarehouse(userId, id)).collect(Collectors.toList());
    }

    private Warehouse updateWarehouseByRequest(@NonNull Warehouse warehouse, @NonNull WarehouseRequest request, @NonNull User gm) {
        Optional.ofNullable(request.getName()).ifPresent(warehouse::setName);
        Optional.ofNullable(request.getShortName()).ifPresent(warehouse::setShortName);
        Optional.ofNullable(request.getAddress()).ifPresent(warehouse::setAddress);
        Optional.ofNullable(request.getPhone()).ifPresent(warehouse::setPhone);
        Optional.ofNullable(request.getDescription()).ifPresent(warehouse::setDescription);
        Optional.ofNullable(request.getActive()).ifPresent(warehouse::setIsActive);
        warehouse.setGeneralManager(gm);
        return warehouseRepository.save(warehouse);
    }
}
