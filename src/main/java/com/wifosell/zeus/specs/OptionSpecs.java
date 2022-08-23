package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionModel_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OptionSpecs {
    public static Specification<OptionModel> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(OptionModel_.ID), id);
        });
    }

    public static Specification<OptionModel> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(OptionModel_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<OptionModel> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(OptionModel_.IS_ACTIVE), true);
            return root.get(OptionModel_.IS_ACTIVE).in(isActives);
        });
    }
}
