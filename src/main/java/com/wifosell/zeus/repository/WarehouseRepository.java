package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Repository
public interface WarehouseRepository extends SoftDeleteCrudRepository<Warehouse, Long> {
    default Warehouse getWarehouseById(Long warehouseId) {
        return findById(warehouseId).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.WAREHOUSE_NOT_FOUND))
        );
    }
}
