package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.category.Category_;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory_;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

public class LazadaCategoryAndSysCategorySpecs {
    public static Specification<LazadaCategoryAndSysCategory> hasSysCategoryId(Long sysCategoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (sysCategoryId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaCategoryAndSysCategory_.SYS_CATEGORY).get(Category_.ID), sysCategoryId);
        });
    }

    public static Specification<LazadaCategoryAndSysCategory> hasLazadaCategoryId(Long lazadaCategoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (lazadaCategoryId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaCategoryAndSysCategory_.LAZADA_CATEGORY).get(LazadaCategory_.ID), lazadaCategoryId);
        });
    }

    public static Specification<LazadaCategoryAndSysCategory> hasGeneralManagerId(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaCategoryAndSysCategory_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }
}
