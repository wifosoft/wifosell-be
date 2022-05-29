package com.wifosell.zeus.repository.ecom_sync;


import com.wifosell.framework.repository.PGMSoftRepository;
import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_account.EcomAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface EcomAccountRepository extends SoftRepository<EcomAccount, Long> {
}
