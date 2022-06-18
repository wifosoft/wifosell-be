package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.EcomAccount_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

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
