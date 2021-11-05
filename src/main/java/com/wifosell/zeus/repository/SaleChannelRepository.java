package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.salechannel.SaleChannel;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Repository
public interface SaleChannelRepository extends JpaRepository<SaleChannel, Long> {
    default SaleChannel getSaleChannelById(Long saleChannelId) {
        return findById(saleChannelId).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.WAREHOUSE_NOT_FOUND))
        );
    }
}
