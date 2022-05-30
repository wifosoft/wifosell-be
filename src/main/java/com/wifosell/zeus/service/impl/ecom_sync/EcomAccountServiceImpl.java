package com.wifosell.zeus.service.impl.ecom_sync;


import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.EcomAccountRepository;
import com.wifosell.zeus.service.EcomService;
import com.wifosell.zeus.specs.CustomerSpecs;
import com.wifosell.zeus.specs.EcomAccountSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class EcomAccountServiceImpl implements EcomService {
    @Autowired
    EcomAccountRepository ecomAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<EcomAccount> getListEcomAccount(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();

        List<EcomAccount> listEcomUser = ecomAccountRepository.findAll(
                EcomAccountSpecs.hasGeneralManager(gmId)
        );
        return listEcomUser;
    }
}
