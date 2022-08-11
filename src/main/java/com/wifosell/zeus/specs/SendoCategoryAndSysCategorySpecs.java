package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.category.Category_;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory_;
import com.wifosell.zeus.model.ecom_sync.SendoCategory_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

public class SendoCategoryAndSysCategorySpecs {
    public static Specification<SendoCategoryAndSysCategory> hasSysCategoryId(Long sysCategoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (sysCategoryId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SendoCategoryAndSysCategory_.SYS_CATEGORY).get(Category_.ID), sysCategoryId);
        });
    }

    public static Specification<SendoCategoryAndSysCategory> hasSendoCategoryId(Long sendoCategoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (sendoCategoryId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SendoCategoryAndSysCategory_.SENDO_CATEGORY).get(SendoCategory_.ID), sendoCategoryId);
        });
    }

    public static Specification<SendoCategoryAndSysCategory> hasGeneralManagerId(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SendoCategoryAndSysCategory_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }
}
