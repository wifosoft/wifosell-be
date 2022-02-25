package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.repository.CustomerRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("Customer")
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getCustomersByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return customerRepository.findCustomersByGeneralManagerId(gm.getId());
    }

    @Override
    public List<Customer> getCustomersByShopId(Long shopId) {
        return customerRepository.findCustomersByShopId(shopId);
    }

    @Override
    public Customer getCustomer(Long channelId) {
        return customerRepository.findCustomerById(channelId);
    }

    @Override
    public Customer addCustomer(Long userId, CustomerRequest customerRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = new Customer();
        this.updateCustomerByRequest(customer, customerRequest);
        customer.setGeneralManager(gm);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long customerId, CustomerRequest channelRequest) {
        Customer customer = customerRepository.findCustomerById(customerId);
        this.updateCustomerByRequest(customer, channelRequest);
        return customerRepository.save(customer);
    }

    @Override
    public Customer activateCustomer(Long customerId) {
        Customer customer = customerRepository.findCustomerById(customerId, true);
        customer.setIsActive(true);
        return customerRepository.save(customer);
    }

    @Override
    public Customer deactivateCustomer(Long customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        customer.setIsActive(false);
        return customerRepository.save(customer);
    }

    private void updateCustomerByRequest(Customer customer, CustomerRequest channelRequest) {
        Optional.ofNullable(channelRequest.getFullname()).ifPresent(customer::setFullname);
    }
}
