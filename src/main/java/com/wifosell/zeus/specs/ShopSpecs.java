package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.Shop_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ShopSpecs {
    public static Specification<Shop> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Shop_.ID), id);
        });
    }

    public static Specification<Shop> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Shop_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Shop> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(Shop_.IS_ACTIVE), true);
            return root.get(Shop_.IS_ACTIVE).in(isActives);
        });
    }
}
