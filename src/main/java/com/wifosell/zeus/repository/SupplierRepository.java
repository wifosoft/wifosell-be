package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.supplier.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends GMSoftRepository<Supplier, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SUPPLIER_NOT_FOUND;
    }
}
