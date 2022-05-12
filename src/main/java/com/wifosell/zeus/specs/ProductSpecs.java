package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Product_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecs {
    public static Specification<Product> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Product_.ID), id);
        });
    }

    public static Specification<Product> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Product_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Product> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(Product_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Product> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Product_.IS_ACTIVE), isActive);
        });
    }
}
