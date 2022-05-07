package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.order.OrderModel;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends SoftRepository<OrderModel, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ORDER_NOT_FOUND;
    }
}
