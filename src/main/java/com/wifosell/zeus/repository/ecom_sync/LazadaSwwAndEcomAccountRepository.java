package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndAttribute;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaSwwAndEcomAccountRepository extends SoftRepository<LazadaSwwAndEcomAccount, Long> {

    @Query("select u from LazadaSwwAndEcomAccount u where u.saleChannelShop.id= ?1 and u.ecomAccount.id=?2")
    Optional<LazadaSwwAndEcomAccount> getRecordBySswIdAndEcomAccountId(Long sswId, Long ecomAccountId);
}
