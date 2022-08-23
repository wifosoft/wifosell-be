package com.wifosell.zeus.specs;



import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.category.Category_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CategorySpecs {
    public static Specification<Category> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Category_.ID), id);
        });
    }

    public static Specification<Category> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Category_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Category> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(Category_.IS_ACTIVE), true);
            return root.get(Category_.IS_ACTIVE).in(isActives);
        });
    }
}
