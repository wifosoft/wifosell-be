package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategory_;
import org.springframework.data.jpa.domain.Specification;

public class SendoCategorySpecs {
    public static Specification<SendoCategory> isLeaf() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SendoCategory_.LEAF), true);
    }

    public static Specification<SendoCategory> isRoot() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(SendoCategory_.PARENT), null);
    }
}
