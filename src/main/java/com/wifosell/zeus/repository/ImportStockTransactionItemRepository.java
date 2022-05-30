package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.stock.ImportStockTransactionItem;

public interface ImportStockTransactionItemRepository extends SoftRepository<ImportStockTransactionItem, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.IMPORT_STOCK_TRANSACTION_ITEM_NOT_FOUND;
    }
}
