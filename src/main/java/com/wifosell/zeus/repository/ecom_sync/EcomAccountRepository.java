package com.wifosell.zeus.repository.ecom_sync;


import com.wifosell.framework.repository.PGMSoftRepository;
import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_account.EcomAccount;
import com.wifosell.zeus.model.user.User;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface EcomAccountRepository extends SoftRepository<EcomAccount, Long> {
    Optional<EcomAccount> findByAccountNameAndEcomName(@NotBlank String accountName, @NotBlank EcomAccount.EcomName ecomName);

}
