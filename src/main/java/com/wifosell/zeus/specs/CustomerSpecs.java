package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.customer.Customer_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CustomerSpecs {
    public static Specification<Customer> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Customer_.ID), id);
        });
    }

    public static Specification<Customer> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Customer_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Customer> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(Customer_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Customer> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Customer_.IS_ACTIVE), isActive);
        });
    }
}
