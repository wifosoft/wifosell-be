package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.supplier.Supplier_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SupplierSpecs {
    public static Specification<Supplier> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Supplier_.ID), id);
        });
    }

    public static Specification<Supplier> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Supplier_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Supplier> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(Supplier_.IS_ACTIVE), true);
            return root.get(Supplier_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Supplier> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Supplier_.IS_ACTIVE), isActive);
        });
    }
}
