package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.EcomAccount_;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

public class LazadaVariantSpecs {
    public static Specification<LazadaVariant> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaVariant_.ID), id);
        });
    }

    public static Specification<LazadaVariant> hasEcomAccountId(Long ecomAccountId) {
        return (root, query, criteriaBuilder) -> {
            if (ecomAccountId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaVariant_.ECOM_ACCOUNT).get(EcomAccount_.ID), ecomAccountId);
        };
    }

    public static Specification<LazadaVariant> hasGeneralManagerId(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaVariant_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }
}
