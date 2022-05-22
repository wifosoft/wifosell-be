package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.invoice.Invoice;
import com.wifosell.zeus.model.invoice.Invoice_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class InvoiceSpecs {
    public static Specification<Invoice> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Invoice_.ID), id);
        });
    }

    public static Specification<Invoice> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Invoice_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Invoice> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(Invoice_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Invoice> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Invoice_.IS_ACTIVE), isActive);
        });
    }
}
