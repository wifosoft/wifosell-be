package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.warehouse.Warehouse;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Repository
public interface WarehouseRepository extends SoftRepository<Warehouse, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.WAREHOUSE_NOT_FOUND;
    }
}
