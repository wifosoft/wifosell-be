package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.GMSoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.shop.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends GMSoftRepository<Shop, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.SHOP_NOT_FOUND;
    }
}
