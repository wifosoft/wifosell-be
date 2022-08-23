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

import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
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

    public static Specification<Product> hasStocks(List<Long> warehouseIds, Integer minQuantity, Integer maxQuantity) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();
            if (warehouseIds != null)
                predicates.add(root.join(Product_.VARIANTS).join(Variant_.STOCKS).get(Stock_.WAREHOUSE).get(Warehouse_.ID).in(warehouseIds));
            if (minQuantity != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.join(Product_.VARIANTS).join(Variant_.STOCKS).get(Stock_.QUANTITY), minQuantity));
            if (maxQuantity != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.join(Product_.VARIANTS).join(Variant_.STOCKS).get(Stock_.QUANTITY), maxQuantity));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Product> betweenTwoDates(Instant from, Instant to) {
        return ((root, query, criteriaBuilder) -> {
            if (from == null || to == null)
                return criteriaBuilder.and();
            return criteriaBuilder.between(root.get(Product_.CREATED_AT), from, to);
        });
    }

}
