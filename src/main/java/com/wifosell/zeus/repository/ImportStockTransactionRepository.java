package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.stock.ImportStockTransaction;

public interface ImportStockTransactionRepository extends SoftRepository<ImportStockTransaction, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.IMPORT_STOCK_TRANSACTION_NOT_FOUND;
    }
}
