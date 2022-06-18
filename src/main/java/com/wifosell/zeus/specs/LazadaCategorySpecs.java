package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory_;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct_;
import org.springframework.data.jpa.domain.Specification;

public class LazadaCategorySpecs {
    public static Specification<LazadaCategory> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaCategory_.ID), id);
        });
    }

    public static Specification<LazadaCategory> isLeaf(boolean isLeaf) {
        return (root, query, criteriaBuilder) -> {
//            if (!isLeaf)
//                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(LazadaCategory_.LEAF) , true);
        };
    }
    
}
