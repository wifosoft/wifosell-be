package com.wifosell.zeus.repository.ecom_sync;


import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Repository
public interface EcomAccountRepository extends SoftRepository<EcomAccount, Long> {
    Optional<EcomAccount> findByAccountNameAndEcomName(@NotBlank String accountName, @NotBlank EcomAccount.EcomName ecomName);



    @Query("select e from EcomAccount e where e.generalManager.id = ?1 and e.ecomName = ?2")
    List<EcomAccount> findAllByGeneralManagerAndEcomName(Long gmId, EcomAccount.EcomName ecomName);


    default EcomAccount getEcomAccountById(Long id) {
        return findById(id).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.ECOM_ACCOUNT_NOT_FOUND))
        );
    }
}
