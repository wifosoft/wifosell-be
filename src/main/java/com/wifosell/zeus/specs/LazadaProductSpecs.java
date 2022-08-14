package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.EcomAccount_;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

public class LazadaProductSpecs {
    public static Specification<LazadaProduct> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaProduct_.ID), id);
        });
    }

    public static Specification<LazadaProduct> hasEcomAccountId(Long ecomAccountId) {
        return (root, query, criteriaBuilder) -> {
            if (ecomAccountId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaProduct_.ECOM_ACCOUNT).get(EcomAccount_.ID), ecomAccountId);
        };
    }

    public static Specification<LazadaProduct> hasGeneralManagerId(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaProduct_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }
}
