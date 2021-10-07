package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import java.awt.*;
import java.util.Optional;

@ApiIgnore
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    default Warehouse getWarehouseById(Long warehouseId) {
        return findById(warehouseId).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.WAREHOUSE_NOT_FOUND))
        );
    }
}
