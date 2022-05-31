package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Product_;
import com.wifosell.zeus.model.product.Variant_;
import com.wifosell.zeus.model.sale_channel.SaleChannel_;
import com.wifosell.zeus.model.shop.SaleChannelShop_;
import com.wifosell.zeus.model.shop.Shop_;
import com.wifosell.zeus.model.stock.Stock_;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.model.warehouse.Warehouse_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import java.util.List;

public class ProductSpecs {
    public static Specification<Product> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Product_.ID), id);
        });
    }

    public static Specification<Product> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(Product_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<Product> inIsActives(List<Boolean> isActives) {
        return ((root, query, criteriaBuilder) -> {
            if (isActives == null || isActives.isEmpty())
                return criteriaBuilder.equal(root.get(Product_.IS_ACTIVE), true);
            return root.get(Product_.IS_ACTIVE).in(isActives);
        });
    }

    public static Specification<Product> inShops(List<Long> shopIds) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            if (shopIds == null || shopIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(Product_.VARIANTS)
                    .join(Variant_.STOCKS)
                    .join(Stock_.WAREHOUSE)
                    .join(Warehouse_.SALE_CHANNEL_SHOPS)
                    .get(SaleChannelShop_.SHOP)
                    .get(Shop_.ID)
                    .in(shopIds);
        });
    }

    public static Specification<Product> inSaleChannels(List<Long> saleChannelIds) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            if (saleChannelIds == null || saleChannelIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(Product_.VARIANTS)
                    .join(Variant_.STOCKS)
                    .join(Stock_.WAREHOUSE)
                    .join(Warehouse_.SALE_CHANNEL_SHOPS)
                    .get(SaleChannelShop_.SALE_CHANNEL)
                    .get(SaleChannel_.ID)
                    .in(saleChannelIds);
        });
    }

    public static Specification<Product> inWarehouses(List<Long> warehouseIds) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            if (warehouseIds == null || warehouseIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(Product_.VARIANTS)
                    .join(Variant_.STOCKS)
                    .get(Stock_.WAREHOUSE)
                    .get(Warehouse_.ID)
                    .in(warehouseIds);
        });
    }

    public static Specification<Product> hasQuantityBetween(Integer minQuantity, Integer maxQuantity) {
        return ((root, query, criteriaBuilder) -> {
            if (minQuantity == null && maxQuantity == null)
                return criteriaBuilder.and();

            Integer finalMinQuantity = minQuantity != null ? minQuantity : 0;

            Path<Integer> quantity = root.join(Product_.VARIANTS).join(Variant_.STOCKS).get(Stock_.QUANTITY);
            query.distinct(true);

            return maxQuantity != null ?
                    criteriaBuilder.between(quantity, finalMinQuantity, maxQuantity) :
                    criteriaBuilder.greaterThanOrEqualTo(quantity, finalMinQuantity);
        });
    }
}
