package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.payload.request.supplier.ISupplierRequest;
import com.wifosell.zeus.payload.request.supplier.UpdateSupplierRequest;
import com.wifosell.zeus.repository.SupplierRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.SupplierService;
import com.wifosell.zeus.specs.SupplierSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("SupplierService")
@Transactional
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Supplier> getSuppliers(
            Long userId,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return supplierRepository.findAll(
                SupplierSpecs.hasGeneralManager(gmId)
                        .and(SupplierSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Supplier getSupplier(Long userId, @NonNull Long supplierId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return supplierRepository.getOne(
                SupplierSpecs.hasGeneralManager(gmId)
                        .and(SupplierSpecs.hasId(supplierId))
        );
    }

    @Override
    public Supplier addSupplier(Long userId, AddSupplierRequest request) {
        User gm = userRepository.getUserById(userId);
        Supplier warehouse = new Supplier();
        return this.updateSupplierByRequest(warehouse, request, gm);
    }

    @Override
    public Supplier updateSupplier(Long userId, @NonNull Long supplierId, UpdateSupplierRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Supplier warehouse = this.getSupplier(userId, supplierId);
        return this.updateSupplierByRequest(warehouse, request, gm);
    }

    @Override
    public Supplier activateSupplier(Long userId, @NonNull Long supplierId) {
        Supplier warehouse = this.getSupplier(userId, supplierId);
        warehouse.setIsActive(true);
        return supplierRepository.save(warehouse);
    }

    @Override
    public Supplier deactivateSupplier(Long userId, @NonNull Long supplierId) {
        Supplier warehouse = this.getSupplier(userId, supplierId);
        warehouse.setIsActive(false);
        return supplierRepository.save(warehouse);
    }

    @Override
    public List<Supplier> activateSuppliers(Long userId, @NonNull List<Long> supplierIds) {
        return supplierIds.stream().map(id -> this.activateSupplier(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Supplier> deactivateSuppliers(Long userId, @NonNull List<Long> supplierIds) {
        return supplierIds.stream().map(id -> this.deactivateSupplier(userId, id)).collect(Collectors.toList());
    }

    private Supplier updateSupplierByRequest(@NonNull Supplier supplier, @NonNull ISupplierRequest request, @NonNull User gm) {
        Optional.ofNullable(request.getName()).ifPresent(supplier::setName);
        Optional.ofNullable(request.getPhone()).ifPresent(supplier::setPhone);
        Optional.ofNullable(request.getEmail()).ifPresent(supplier::setEmail);
        Optional.ofNullable(request.getNation()).ifPresent(supplier::setNation);
        Optional.ofNullable(request.getCity()).ifPresent(supplier::setCity);
        Optional.ofNullable(request.getDistrict()).ifPresent(supplier::setDistrict);
        Optional.ofNullable(request.getWard()).ifPresent(supplier::setWard);
        Optional.ofNullable(request.getAddressDetail()).ifPresent(supplier::setAddressDetail);
        Optional.ofNullable(request.getIsActive()).ifPresent(supplier::setIsActive);
        supplier.setGeneralManager(gm);
        return supplierRepository.save(supplier);
    }
}
