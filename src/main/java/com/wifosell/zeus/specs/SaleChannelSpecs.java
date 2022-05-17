package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.sale_channel.SaleChannel_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SaleChannelSpecs {
    public static Specification<SaleChannel> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SaleChannel_.ID), id);
        });
    }

    public static Specification<SaleChannel> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SaleChannel_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<SaleChannel> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(SaleChannel_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<SaleChannel> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(SaleChannel_.IS_ACTIVE), isActive);
        });
    }
}
