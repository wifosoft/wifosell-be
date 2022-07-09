package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaProductRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaVariantRepository;
import com.wifosell.zeus.service.LazadaProductService;
import com.wifosell.zeus.specs.LazadaProductSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LazadaProductServiceImpl implements LazadaProductService {
    @Autowired
    LazadaVariantRepository lazadaVariantRepository;

    @Autowired
    LazadaProductRepository lazadaProductRepository;

    @Autowired
    EcomAccountRepository ecomAccountRepository;

    public Page<LazadaProduct> getProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getEcomAccountById(ecomId).getId();
        return lazadaProductRepository.findAll(
                LazadaProductSpecs.inEcomAccount(ecomIdCk),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Page<LazadaVariant> getVariants(Long ecomId, int offset, int limit, String sortBy, String orderBy) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getEcomAccountById(ecomId).getId();
        return lazadaVariantRepository.findAll(
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

}
