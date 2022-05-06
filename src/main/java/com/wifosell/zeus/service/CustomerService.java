package com.wifosell.zeus.service;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface CustomerService {
    Page<Customer> getAllCustomers(Boolean isActive, int offset, int limit, String sortBy, String orderBy);

    Page<Customer> getCustomers(@NonNull Long userId, Boolean isActive, int offset, int limit, String sortBy, String orderBy);

    Customer getCustomer(@NonNull Long userId, @NonNull Long customerId);

    Customer addCustomer(@NonNull Long userId, @Valid CustomerRequest customerRequest);

    Customer updateCustomer(@NonNull Long userId, @NonNull Long customerId, @Valid CustomerRequest customerRequest);

    Customer activateCustomer(@NonNull Long userId, @NonNull Long customerId);

    Customer deactivateCustomer(@NonNull Long userId, @NonNull Long customerId);

    List<Customer> activateCustomers(@NonNull Long userId, @NonNull List<Long> customerIds);

    List<Customer> deactivateCustomers(@NonNull Long userId, @NonNull List<Long> customerIds);
}
