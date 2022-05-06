package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.repository.CustomerRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.CustomerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("Customer")
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public Page<Customer> getAllCustomers(
            Boolean isActive,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.fromString(orderBy), sortBy));
        if (isActive == null)
            return customerRepository.findAll(pageable);
        return customerRepository.findAndPaginateAllWithActive(isActive, pageable);
    }

    @Override
    public Page<Customer> getCustomers(
            @NonNull Long userId,
            Boolean isActive,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.fromString(orderBy), sortBy));
        if (isActive == null)
            return customerRepository.findAndPaginateAllWithGm(gm.getId(), pageable);
        return customerRepository.findAndPaginateAllWithGmAndActive(gm.getId(), isActive, pageable);
    }

    @Override
    public Customer getCustomer(
            @NonNull Long userId,
            @NonNull Long customerId
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return customerRepository.getByIdWithGm(gm.getId(), customerId);
    }

    @Override
    public Customer addCustomer(@NonNull Long userId, @Valid CustomerRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = new Customer();
        return this.updateCustomerByRequest(customer, request, gm);
    }

    @Override
    public Customer updateCustomer(@NonNull Long userId, @NonNull Long customerId, @Valid CustomerRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = customerRepository.getByIdWithGm(gm.getId(), customerId);
        return this.updateCustomerByRequest(customer, request, gm);
    }

    @Override
    public Customer activateCustomer(@NonNull Long userId, @NonNull Long customerId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = customerRepository.getByIdWithGm(gm.getId(), customerId);
        customer.setIsActive(true);
        return customerRepository.save(customer);
    }

    @Override
    public Customer deactivateCustomer(@NonNull Long userId, @NonNull Long customerId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = customerRepository.getByIdWithGm(gm.getId(), customerId);
        customer.setIsActive(false);
        return customerRepository.save(customer);
    }

    public List<Customer> activateCustomers(@NonNull Long userId, @NonNull List<Long> customerIds) {
        return customerIds.stream().map(id -> this.activateCustomer(userId, id)).collect(Collectors.toList());
    }

    public List<Customer> deactivateCustomers(@NonNull Long userId, @NonNull List<Long> customerIds) {
        return customerIds.stream().map(id -> this.deactivateCustomer(userId, id)).collect(Collectors.toList());
    }

    private Customer updateCustomerByRequest(Customer customer, CustomerRequest request, User gm) {
        Optional.ofNullable(request.getFullName()).ifPresent(customer::setFullName);
        Optional.ofNullable(request.getDob()).ifPresent(customer::setDob);
        Optional.ofNullable(request.getSex()).ifPresent(customer::setSex);
        Optional.ofNullable(request.getPhone()).ifPresent(customer::setPhone);
        Optional.ofNullable(request.getEmail()).ifPresent(customer::setEmail);
        Optional.ofNullable(request.getCin()).ifPresent(customer::setCin);
        Optional.ofNullable(request.getNation()).ifPresent(customer::setNation);
        Optional.ofNullable(request.getCity()).ifPresent(customer::setCity);
        Optional.ofNullable(request.getDistrict()).ifPresent(customer::setDistrict);
        Optional.ofNullable(request.getWard()).ifPresent(customer::setWard);
        Optional.ofNullable(request.getAddressDetail()).ifPresent(customer::setAddressDetail);
        Optional.ofNullable(request.getActive()).ifPresent(customer::setIsActive);
        customer.setGeneralManager(gm);
        return customerRepository.save(customer);
    }
}
