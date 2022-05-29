package com.wifosell.zeus.service.impl.ecom_sync;


import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.service.EcomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class EcomAccountServiceImpl implements EcomService {
    EcomAccountRepository ecomAccountRepository;
    UserRepository userRepository;

    @Override
    public List<EcomAccount> getListEcomAccount(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return listAccount;
    }
}
