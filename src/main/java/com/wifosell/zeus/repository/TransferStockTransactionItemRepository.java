package com.wifosell.zeus.repository;

import com.wifosell.framework.repository.SoftRepository;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.model.stock.TransferStockTransactionItem;

public interface TransferStockTransactionItemRepository extends SoftRepository<TransferStockTransactionItem, Long> {
    default EAppExceptionCode getExceptionCodeEntityNotFound() {
        return EAppExceptionCode.TRANSFER_STOCK_TRANSACTION_ITEM_NOT_FOUND;
    }
}
