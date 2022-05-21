package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.model.warehouse.Warehouse_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class WarehouseSpecs {
    public static Specification<Warehouse> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Warehouse_.ID), id);
        });
    }

    public static Specification<Warehouse> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Warehouse_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Warehouse> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(Warehouse_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Warehouse> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Warehouse_.IS_ACTIVE), isActive);
        });
    }
}
