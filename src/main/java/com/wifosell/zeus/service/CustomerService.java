package com.wifosell.zeus.service;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    List<Customer> getCustomersByUserId(Long userId);
    List<Customer> getCustomersByShopId(Long shopId);
    Customer getCustomer(Long customerId);
    Customer addCustomer(Long userId, CustomerRequest customerRequest);
    Customer updateCustomer(Long customerId, CustomerRequest customerRequest);
    Customer activateCustomer(Long customerId);
    Customer deactivateCustomer(Long customerId);
}
