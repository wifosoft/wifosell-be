package com.wifosell.zeus.service;

import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.RegisterRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public interface EcomService {
    List<EcomAccount> getListEcomAccount(Long userId);
}
