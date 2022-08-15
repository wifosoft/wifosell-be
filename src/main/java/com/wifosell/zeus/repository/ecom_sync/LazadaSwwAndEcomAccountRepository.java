package com.wifosell.zeus.repository.ecom_sync;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LazadaSwwAndEcomAccountRepository extends SoftRepository<LazadaSwwAndEcomAccount, Long> {
    @Override
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.ECOM_ACCOUNT_LINK_SALE_CHANNEL_SHOP_NOT_FOUND;
    }

    @Query("select u from LazadaSwwAndEcomAccount u where u.saleChannelShop.id= ?1 and u.ecomAccount.id=?2")
    Optional<LazadaSwwAndEcomAccount> getRecordBySswIdAndEcomAccountId(Long sswId, Long ecomAccountId);

    void deleteByEcomAccount(Long ecomId);

    void deleteBySaleChannelShop(Long sswId);


    @Query("select u from LazadaSwwAndEcomAccount u where u.ecomAccount.id= ?1")
    Optional<LazadaSwwAndEcomAccount> findByEcomAccountId(Long ecomAccountId);

    default LazadaSwwAndEcomAccount getByEcomAccountId(Long ecomId) {
        return findByEcomAccountId(ecomId).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(this.getExceptionCodeEntityNotFound()))
        );
    }
}
