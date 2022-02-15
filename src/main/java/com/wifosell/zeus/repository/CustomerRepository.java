package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends SoftDeleteCrudRepository<Customer, Long>{
    default Customer findCustomerById(Long customerId) {
        return this.findCustomerById(customerId, false);
    }

    default Customer findCustomerById(Long customerId, boolean includeInactive) {
        Optional<Customer> optionalChannel = includeInactive ? this.findById(customerId) : this.findOne(customerId);
        return optionalChannel.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_NOT_FOUND))
        );
    }

    @Transactional
    @Query("select c from Customer c where c.isActive = true and c.generalManager.id = ?1")
    List<Customer> findCustomersByGeneralManagerId(Long generalManagerId);

    @Transactional
    @Query("select sc from Customer sc join SaleChannelShopRelation scsr on sc.id = scsr.saleChannel.id where sc.isActive = true and scsr.shop.id = ?1")
    List<Customer> findCustomersByShopId(Long shopId);
}
