package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndAttribute;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface LazadaSwwAndEcomAccountRepository extends SoftRepository<LazadaSwwAndEcomAccount, Long> {

    @Query("select u from LazadaSwwAndEcomAccount u where u.saleChannelShop.id= ?1 and u.ecomAccount.id=?2")
    Optional<LazadaSwwAndEcomAccount> getRecordBySswIdAndEcomAccountId(Long sswId, Long ecomAccountId);

    @Query("select u from LazadaSwwAndEcomAccount u where u.ecomAccount.id= ?1")
    Optional<LazadaSwwAndEcomAccount> findByEcomAccountId(Long ecomAccountId);
}
