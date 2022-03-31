package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.order.OrderItem;

public interface OrderItemRepository extends SoftRepository<OrderItem, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ORDER_ITEM_NOT_FOUND;
    }

    void deleteAllByOrderId(Long orderId);
}
