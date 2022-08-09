package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.ecom_sync.LazadaVariant;
import com.wifosell.zeus.model.ecom_sync.SendoProduct;
import com.wifosell.zeus.model.ecom_sync.SendoVariant;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaProductRepository;
import com.wifosell.zeus.repository.ecom_sync.SendoProductRepository;
import com.wifosell.zeus.repository.ecom_sync.SendoVariantRepository;
import com.wifosell.zeus.service.LazadaProductService;
import com.wifosell.zeus.service.SendoProductService;
import com.wifosell.zeus.specs.LazadaProductSpecs;
import com.wifosell.zeus.specs.SendoProductSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SendoProductServiceImpl implements SendoProductService {
    @Autowired
    SendoVariantRepository sendoVariantRepository;

    @Autowired
    SendoProductRepository sendoProductRepository;

    @Autowired
    EcomAccountRepository ecomAccountRepository;

    public Page<SendoProduct> getProducts(
            Long ecomId,
            int offset,
            int limit,
            String sortBy,
            String orderBy
    ) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getEcomAccountById(ecomId).getId();
        return sendoProductRepository.findAll(
                SendoProductSpecs.inEcomAccount(ecomIdCk),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public Page<SendoVariant> getVariants(Long ecomId, int offset, int limit, String sortBy, String orderBy) {
        Long ecomIdCk = ecomId == null ? null : ecomAccountRepository.getEcomAccountById(ecomId).getId();
        return sendoVariantRepository.findAll(
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }
}
