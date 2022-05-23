package com.wifosell.zeus.service;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface CustomerService {
    Page<Customer> getCustomers(Long userId, List<Boolean> isActives,
                                Integer offset, Integer limit, String sortBy, String orderBy);

    Customer getCustomer(Long userId, @NonNull Long customerId);

    Customer addCustomer(Long userId, @Valid CustomerRequest request);

    Customer updateCustomer(Long userId, @NonNull Long customerId, @Valid CustomerRequest request);

    Customer activateCustomer(Long userId, @NonNull Long customerId);

    Customer deactivateCustomer(Long userId, @NonNull Long customerId);

    List<Customer> activateCustomers(Long userId, @NonNull List<Long> customerIds);

    List<Customer> deactivateCustomers(Long userId, @NonNull List<Long> customerIds);

    List<Customer> searchCustomers(String text);
}
