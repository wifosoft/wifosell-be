package com.wifosell.zeus.repository.ecom_sync;


import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface EcomAccountRepository extends SoftRepository<EcomAccount, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ECOM_ACCOUNT_NOT_FOUND;
    }

    Optional<EcomAccount> findByAccountNameAndEcomName(@NotBlank String accountName, @NotBlank EcomAccount.EcomName ecomName);
}
