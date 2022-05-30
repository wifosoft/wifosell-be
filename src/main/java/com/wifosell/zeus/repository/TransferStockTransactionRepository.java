package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.stock.TransferStockTransaction;

public interface TransferStockTransactionRepository extends SoftRepository<TransferStockTransaction, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.TRANSFER_STOCK_TRANSACTION_NOT_FOUND;
    }
}
