package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.OrderModel_;
import com.wifosell.zeus.model.sale_channel.SaleChannel_;
import com.wifosell.zeus.model.shop.Shop_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OrderSpecs {
    public static Specification<OrderModel> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(OrderModel_.ID), id);
        });
    }

    public static Specification<OrderModel> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(OrderModel_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<OrderModel> inShops(List<Long> shopIds) {
        return (root, query, criteriaBuilder) -> {
            if (shopIds == null || shopIds.isEmpty())
                return criteriaBuilder.and();
            return root.get(OrderModel_.SHOP).get(Shop_.ID).in(shopIds);
        };
    }

    public static Specification<OrderModel> inSaleChannels(List<Long> saleChannelIds) {
        return ((root, query, criteriaBuilder) -> {
            if (saleChannelIds == null || saleChannelIds.isEmpty())
                return criteriaBuilder.and();
            return root.get(OrderModel_.SALE_CHANNEL).get(SaleChannel_.ID).in(saleChannelIds);
        });
    }

    public static Specification<OrderModel> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.and();
            return root.get(OrderModel_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<OrderModel> hasIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(OrderModel_.IS_ACTIVE), isActive);
        });
    }
}
