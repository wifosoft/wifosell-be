package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.sale_channel.SaleChannel_;
import com.wifosell.zeus.model.shop.SaleChannelShop_;
import com.wifosell.zeus.model.shop.Shop_;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.model.warehouse.Warehouse_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class WarehouseSpecs {
    public static Specification<Warehouse> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Warehouse_.ID), id);
        });
    }

    public static Specification<Warehouse> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Warehouse_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Warehouse> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(Warehouse_.IS_ACTIVE), true);
            return root.get(Warehouse_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Warehouse> hasShops(List<Long> shopIds) {
        return ((root, query, criteriaBuilder) -> {
            if (shopIds == null || shopIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(Warehouse_.SALE_CHANNEL_SHOPS)
                    .get(SaleChannelShop_.SHOP)
                    .get(Shop_.ID)
                    .in(shopIds);
        });
    }

    public static Specification<Warehouse> hasSaleChannels(List<Long> saleChannelIds) {
        return ((root, query, criteriaBuilder) -> {
            if (saleChannelIds == null || saleChannelIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(Warehouse_.SALE_CHANNEL_SHOPS)
                    .get(SaleChannelShop_.SALE_CHANNEL)
                    .get(SaleChannel_.ID)
                    .in(saleChannelIds);
        });
    }
}
