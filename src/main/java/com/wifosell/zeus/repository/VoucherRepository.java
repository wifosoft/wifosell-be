package com.wifosell.zeus.repository;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.voucher.Voucher;
import com.wifosell.zeus.payload.GApiErrorBody;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends SoftDeleteCrudRepository<Voucher, Long> {
    default Voucher findVoucherById(Long voucherId) {
        return this.findVoucherById(voucherId, false);
    }

    default Voucher findVoucherById(Long voucherId, boolean includeInactive) {
        Optional<Voucher> optionalVoucher = includeInactive ? this.findById(voucherId) : this.findOne(voucherId);
        return optionalVoucher.orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SALE_CHANNEL_NOT_FOUND))
        );
    }

    @Transactional
    @Query("select v from Voucher v where v.isActive = true and v.generalManager.id = ?1")
    List<Voucher> findVouchersByGeneralManagerId(Long generalManagerId);

    @Transactional
    @Query("select v from Voucher v join SaleChannelShopRelation scsr on v.id = scsr.saleChannel.id where v.isActive = true and scsr.shop.id = ?1")
    List<Voucher> findVouchersByShopId(Long shopId);
}
