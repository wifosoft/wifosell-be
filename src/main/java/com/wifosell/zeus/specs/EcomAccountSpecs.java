package com.wifosell.zeus.specs;

import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.model.ecom_sync.EcomAccount_;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount_;
import com.wifosell.zeus.model.shop.SaleChannelShop_;
import com.wifosell.zeus.model.shop.Shop_;
import com.wifosell.zeus.model.user.User_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class EcomAccountSpecs {
    public static Specification<EcomAccount> hasId(Long id) {
        return ((root, query, criteriaBuilder) -> {
            if (id == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(EcomAccount_.ID), id);
        });
    }

    public static Specification<EcomAccount> hasGeneralManager(Long generalManagerId) {
        return (root, query, criteriaBuilder) -> {
            if (generalManagerId == null)
                return criteriaBuilder.and();
            return criteriaBuilder.equal(root.get(EcomAccount_.GENERAL_MANAGER).get(User_.ID), generalManagerId);
        };
    }

    public static Specification<EcomAccount> hasShopIds(List<Long> shopIds) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            if (shopIds == null || shopIds.isEmpty())
                return criteriaBuilder.and();
            return root.join(EcomAccount_.RELATION_SWWS)
                    .join(LazadaSwwAndEcomAccount_.SALE_CHANNEL_SHOP)
                    .join(SaleChannelShop_.SHOP)
                    .get(Shop_.ID)
                    .in(shopIds);
        };
    }

    public static Specification<EcomAccount> hasEcomNames(List<EcomAccount.EcomName> ecomNames) {
        return (root, query, criteriaBuilder) -> {
            if (ecomNames == null || ecomNames.isEmpty())
                return criteriaBuilder.and();
            return root.get(EcomAccount_.ECOM_NAME).in(ecomNames);
        };
    }
}
