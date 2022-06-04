package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.customer.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends SoftRepository<Customer, Long>{
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.CUSTOMER_NOT_FOUND;
    }
}
