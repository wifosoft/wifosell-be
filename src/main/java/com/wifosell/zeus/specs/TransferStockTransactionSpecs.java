package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.stock.TransferStockTransaction;
import com.wifosell.zeus.model.stock.TransferStockTransaction_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TransferStockTransactionSpecs {
    public static Specification<TransferStockTransaction> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(TransferStockTransaction_.ID), id);
        });
    }

    public static Specification<TransferStockTransaction> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(TransferStockTransaction_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<TransferStockTransaction> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(TransferStockTransaction_.IS_ACTIVE), true);
            return root.get(TransferStockTransaction_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<TransferStockTransaction> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(TransferStockTransaction_.IS_ACTIVE), isActive);
        });
    }

    public static Specification<TransferStockTransaction> inTypes(List<TransferStockTransaction.TYPE> types) {
        return ((root, query, criteriaBuilder) -> {
            if (types == null || types.isEmpty())
                return criteriaBuilder.and();
            return root.get(TransferStockTransaction_.TYPE).in(types);
        });
    }

    public static Specification<TransferStockTransaction> inProcessingStatuses(List<TransferStockTransaction.PROCESSING_STATUS> statuses) {
        return ((root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty())
                return criteriaBuilder.and();
            return root.get(TransferStockTransaction_.PROCESSING_STATUS).in(statuses);
        });
    }
}
