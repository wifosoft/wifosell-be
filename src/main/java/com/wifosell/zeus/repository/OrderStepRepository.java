package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.order.OrderStep;

public interface OrderStepRepository extends SoftRepository<OrderStep, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ORDER_STEP_NOT_FOUND;
    }
}
