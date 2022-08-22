package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.pricetrack.PriceTrack;
import com.wifosell.zeus.model.pricetrack.PriceTrack_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PriceTrackSpecs {
    public static Specification<PriceTrack> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(PriceTrack_.ID), id);
        });
    }

    public static Specification<PriceTrack> hasGeneralManagerId(Long gmId) {
        return (root, query, criteriaBuilder) -> {
            if (gmId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(PriceTrack_.GENERAL_MANAGER).get(User_.ID), gmId);
        };
    }

    public static Specification<PriceTrack> hasActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(PriceTrack_.IS_ACTIVE), true);
            return root.get(PriceTrack_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<PriceTrack> isActive() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PriceTrack_.IS_ACTIVE), true));
    }
}
