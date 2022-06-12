package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.customer.Customer_;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.repository.CustomerRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.CustomerService;
import com.wifosell.zeus.specs.CustomerSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("CustomerService")
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public Page<Customer> getCustomers(
            Long userId,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return customerRepository.findAll(
                CustomerSpecs.hasGeneralManager(gmId)
                        .and(CustomerSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public List<Customer> searchCustomers(Long userId, String keyword, List<Boolean> isActives, Integer offset, Integer limit) {
        SearchSession searchSession = Search.session(entityManager);

        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        if (offset == null) {
            offset = 0;
        }
        if (limit == null || limit > 100) {
            limit = 100;
        }

        return searchSession.search(Customer.class).where(f -> f.bool(b -> {
            b.must(f.matchAll());
            if (gmId != null) {
                b.must(f.match().field(Customer_.GENERAL_MANAGER + "." + User_.ID).matching(gmId));
            }
            if (keyword != null && !keyword.isEmpty()) {
                b.must(f.match().fields(Customer_.FULL_NAME, Customer_.PHONE, Customer_.EMAIL).matching(keyword));
            }
            if (isActives == null || isActives.isEmpty()) {
                b.must(f.match().field(Customer_.IS_ACTIVE).matching(true));
            } else {
                b.must(f.terms().field(Customer_.IS_ACTIVE).matchingAny(isActives));
            }
        })).fetchHits(offset * limit, limit);
    }

    @Override
    public Customer getCustomer(
            Long userId,
            @NonNull Long customerId
    ) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return customerRepository.getOne(
                CustomerSpecs.hasGeneralManager(gmId)
                        .and(CustomerSpecs.hasId(customerId))
        );
    }

    @Override
    public Customer addCustomer(
            Long userId,
            @Valid CustomerRequest request
    ) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = new Customer();
        return this.updateCustomerByRequest(customer, request, gm);
    }

    @Override
    public Customer updateCustomer(Long userId, @NonNull Long customerId, @Valid CustomerRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Customer customer = getCustomer(userId, customerId);
        return this.updateCustomerByRequest(customer, request, gm);
    }

    @Override
    public Customer activateCustomer(Long userId, @NonNull Long customerId) {
        Customer customer = getCustomer(userId, customerId);
        customer.setIsActive(true);
        return customerRepository.save(customer);
    }

    @Override
    public Customer deactivateCustomer(Long userId, @NonNull Long customerId) {
        Customer customer = getCustomer(userId, customerId);
        customer.setIsActive(false);
        return customerRepository.save(customer);
    }

    public List<Customer> activateCustomers(Long userId, @NonNull List<Long> customerIds) {
        return customerIds.stream().map(id -> this.activateCustomer(userId, id)).collect(Collectors.toList());
    }

    public List<Customer> deactivateCustomers(Long userId, @NonNull List<Long> customerIds) {
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
        Optional.ofNullable(request.getIsActive()).ifPresent(customer::setIsActive);
        customer.setGeneralManager(gm);
        return customerRepository.save(customer);
    }
}
