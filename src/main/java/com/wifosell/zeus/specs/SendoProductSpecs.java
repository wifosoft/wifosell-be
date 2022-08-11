package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoProduct_;
import org.springframework.data.jpa.domain.Specification;

public class SendoProductSpecs {
    public static Specification<SendoProduct> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SendoProduct_.ID), id);
        });
    }

    public static Specification<SendoProduct> inEcomAccount(Long ecomAccountId) {
        return (root, query, criteriaBuilder) -> {
            if (ecomAccountId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SendoProduct_.ECOM_ACCOUNT).get(SendoProduct_.ID), ecomAccountId);
        };
    }


}
