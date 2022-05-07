package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.payload.request.supplier.ISupplierRequest;
import com.wifosell.zeus.payload.request.supplier.UpdateSupplierRequest;
import com.wifosell.zeus.repository.SupplierRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.SupplierService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    public List<Supplier> getSuppliers(@NonNull Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return supplierRepository.findAllWithGm(gm.getId());
        return supplierRepository.findAllWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public Supplier getSupplier(@NonNull Long userId, @NonNull Long supplierId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return supplierRepository.getByIdWithGm(gm.getId(), supplierId);
    }

    @Override
    public Supplier addSupplier(@NonNull Long userId, AddSupplierRequest request) {
        User gm = userRepository.getUserById(userId);
        Supplier warehouse = new Supplier();
        return this.updateSupplierByRequest(warehouse, request, gm);
    }

    @Override
    public Supplier updateSupplier(@NonNull Long userId, @NonNull Long supplierId, UpdateSupplierRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Supplier warehouse = supplierRepository.getByIdWithGm(gm.getId(), supplierId);
        this.updateSupplierByRequest(warehouse, request, gm);
        return supplierRepository.save(warehouse);
    }

    @Override
    public Supplier activateSupplier(@NonNull Long userId, @NonNull Long supplierId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Supplier warehouse = supplierRepository.getByIdWithGm(gm.getId(), supplierId);
        warehouse.setIsActive(true);
        return supplierRepository.save(warehouse);
    }

    @Override
    public Supplier deactivateSupplier(@NonNull Long userId, @NonNull Long supplierId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Supplier warehouse = supplierRepository.getByIdWithGm(gm.getId(), supplierId);
        warehouse.setIsActive(false);
        return supplierRepository.save(warehouse);
    }

    @Override
    public List<Supplier> activateSuppliers(@NonNull Long userId, @NonNull List<Long> supplierIds) {
        return supplierIds.stream().map(id -> this.activateSupplier(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Supplier> deactivateSuppliers(@NonNull Long userId, @NonNull List<Long> supplierIds) {
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
