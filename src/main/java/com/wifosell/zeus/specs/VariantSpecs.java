package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.Variant_;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.stock.Stock_;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.model.warehouse.Warehouse_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.function.Predicate;

public class VariantSpecs {
    public static Specification<Variant> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Variant_.ID), id);
        });
    }

    public static Specification<Variant> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Variant_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Variant> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(Variant_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Variant> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Variant_.IS_ACTIVE), isActive);
        });
    }

    public static Specification<Variant> inWarehouses(List<Long> warehouseIds) {
        return ((root, query, criteriaBuilder) -> {
            if (warehouseIds == null || warehouseIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(Variant_.STOCKS).get(Stock_.WAREHOUSE).get(Warehouse_.ID).in(warehouseIds);
        });
    }
}
