package com.wifosell.zeus.service;

import com.wifosell.zeus.model.voucher.Voucher;
import com.wifosell.zeus.payload.request.voucher.VoucherRequest;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAllVouchers();
    List<Voucher> getVouchersByUserId(Long userId);
    List<Voucher> getVouchersByShopId(Long shopId);
    Voucher getVoucher(Long voucherId);
    Voucher addVoucher(Long userId, VoucherRequest voucherRequest);
    Voucher updateVoucher(Long voucherId, VoucherRequest voucherRequest);
    Voucher activateVoucher(Long voucherId);
    Voucher deactivateSaleChannel(Long voucherId);
}
