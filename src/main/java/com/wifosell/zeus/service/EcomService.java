package com.wifosell.zeus.service;

import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface EcomService {
    List<EcomAccount> getListEcomAccount(Long userId);

    EcomAccount addEcomAccountLazada(Long userId, EcomAccount account);

    EcomAccount addEcomAccountLazadaFromCallback(EcomAccountLazadaCallbackPayload payloadCallback) throws ApiException;

    EcomAccount getEcomAccount(Long ecomId);

    boolean deleteEcomAccount(Long ecomId);
    
}
