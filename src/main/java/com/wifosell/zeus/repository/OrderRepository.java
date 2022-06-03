package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.order.OrderModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.Instant;

@Repository
public interface OrderRepository extends SoftRepository<OrderModel, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ORDER_NOT_FOUND;
    }

    @Transactional
    Long countAllByCreatedAtBetween(Instant dateForm, Instant dateTo);

    @Transactional
    Long countAllByShopIdAndCreatedAtBetween(Long shopId, Instant dateForm, Instant dateTo);

    @Query (value = "SELECT sum(subtotal) FROM OrderModel")
    Long sumTotalOrder(@Nullable Specification var1);


}
