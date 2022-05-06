package com.wifosell.zeus.service;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.payload.request.supplier.UpdateSupplierRequest;
import lombok.NonNull;

import javax.validation.Valid;
import java.util.List;

public interface SupplierService {
    List<Supplier> getSuppliers(@NonNull Long userId, Boolean isActive);

    Supplier getSupplier(@NonNull Long userId, @NonNull Long supplierId);

    Supplier addSupplier(@NonNull Long userId, @Valid AddSupplierRequest request);

    Supplier updateSupplier(@NonNull Long userId, @NonNull Long supplierId, @Valid UpdateSupplierRequest request);

    Supplier activateSupplier(@NonNull Long userId, @NonNull Long supplierId);

    Supplier deactivateSupplier(@NonNull Long userId, @NonNull Long supplierId);

    List<Supplier> activateSuppliers(@NonNull Long userId, @NonNull List<Long> supplierIds);

    List<Supplier> deactivateSuppliers(@NonNull Long userId, @NonNull List<Long> supplierIds);
}
