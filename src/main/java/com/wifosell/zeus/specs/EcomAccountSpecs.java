package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.model.ecom_account.EcomAccount_;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.model.customer.Customer_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class EcomAccountSpecs {
    public static Specification<EcomAccount> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(EcomAccount_.ID), id);
        });
    }

    public static Specification<EcomAccount> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(EcomAccount_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

}
