package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.WarehouseRepository;
import com.wifosell.zeus.service.WarehouseService;
import com.wifosell.zeus.specs.WarehouseSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<Warehouse> getWarehouses(
            Long userId,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return warehouseRepository.findAll(
                WarehouseSpecs.hasGeneralManager(gmId)
                        .and(WarehouseSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public List<Warehouse> getWarehousesByShopIdsAndSaleChannelIds(List<Long> shopIds, List<Long> saleChannelIds) {
        return warehouseRepository.findAll(
                WarehouseSpecs.inShops(shopIds)
                        .and(WarehouseSpecs.inSaleChannels(saleChannelIds))
        );
    }

    @Override
    public Warehouse getWarehouse(Long userId, @NonNull Long warehouseId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return warehouseRepository.getOne(
                WarehouseSpecs.hasGeneralManager(gmId)
                        .and(WarehouseSpecs.hasId(warehouseId))
        );
    }

    @Override
    public Warehouse addWarehouse(Long userId, @NonNull WarehouseRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = new Warehouse();
        return this.updateWarehouseByRequest(warehouse, request, gm);
    }

    @Override
    public Warehouse updateWarehouse(Long userId, @NonNull Long warehouseId, @NonNull WarehouseRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = this.getWarehouse(userId, warehouseId);
        return this.updateWarehouseByRequest(warehouse, request, gm);
    }

    @Override
    public Warehouse activateWarehouse(Long userId, @NonNull Long warehouseId) {
        Warehouse warehouse = this.getWarehouse(userId, warehouseId);
        warehouse.setIsActive(true);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse deactivateWarehouse(Long userId, @NonNull Long warehouseId) {
        Warehouse warehouse = this.getWarehouse(userId, warehouseId);
        warehouse.setIsActive(false);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> activateWarehouses(Long userId, @NonNull List<Long> warehouseIds) {
        return warehouseIds.stream().map(id -> this.activateWarehouse(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Warehouse> deactivateWarehouses(Long userId, @NonNull List<Long> warehouseIds) {
        return warehouseIds.stream().map(id -> this.deactivateWarehouse(userId, id)).collect(Collectors.toList());
    }


    private Warehouse updateWarehouseByRequest(@NonNull Warehouse warehouse, @NonNull WarehouseRequest request, @NonNull User gm) {
        Optional.ofNullable(request.getName()).ifPresent(warehouse::setName);
        Optional.ofNullable(request.getShortName()).ifPresent(warehouse::setShortName);
        Optional.ofNullable(request.getAddress()).ifPresent(warehouse::setAddress);
        Optional.ofNullable(request.getPhone()).ifPresent(warehouse::setPhone);
        Optional.ofNullable(request.getDescription()).ifPresent(warehouse::setDescription);
        Optional.ofNullable(request.getIsActive()).ifPresent(warehouse::setIsActive);
        warehouse.setGeneralManager(gm);
        return warehouseRepository.save(warehouse);
    }
}
