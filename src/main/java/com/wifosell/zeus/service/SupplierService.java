package com.wifosell.zeus.service;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.payload.request.supplier.UpdateSupplierRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface SupplierService {
    Page<Supplier> getSuppliers(Long userId, List<Boolean> isActives,
                                Integer offset, Integer limit, String sortBy, String orderBy);

    Supplier getSupplier(Long userId, @NonNull Long supplierId);

    Supplier addSupplier(Long userId, @Valid AddSupplierRequest request);

    Supplier updateSupplier(Long userId, @NonNull Long supplierId, @Valid UpdateSupplierRequest request);

    Supplier activateSupplier(Long userId, @NonNull Long supplierId);

    Supplier deactivateSupplier(Long userId, @NonNull Long supplierId);

    List<Supplier> activateSuppliers(Long userId, @NonNull List<Long> supplierIds);

    List<Supplier> deactivateSuppliers(Long userId, @NonNull List<Long> supplierIds);
}
