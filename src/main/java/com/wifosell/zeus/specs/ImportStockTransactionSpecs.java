package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.model.stock.ImportStockTransaction_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ImportStockTransactionSpecs {
    public static Specification<ImportStockTransaction> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(ImportStockTransaction_.ID), id);
        });
    }

    public static Specification<ImportStockTransaction> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(ImportStockTransaction_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<ImportStockTransaction> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(ImportStockTransaction_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<ImportStockTransaction> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(ImportStockTransaction_.IS_ACTIVE), isActive);
        });
    }

    public static Specification<ImportStockTransaction> inTypes(List<ImportStockTransaction.TYPE> types) {
        return ((root, query, criteriaBuilder) -> {
            if (types == null || types.isEmpty())
                return criteriaBuilder.and();
            return root.get(ImportStockTransaction_.TYPE).in(types);
        });
    }

    public static Specification<ImportStockTransaction> inProcessingStatuses(List<ImportStockTransaction.PROCESSING_STATUS> statuses) {
        return ((root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty())
                return criteriaBuilder.and();
            return root.get(ImportStockTransaction_.PROCESSING_STATUS).in(statuses);
        });
    }
}
