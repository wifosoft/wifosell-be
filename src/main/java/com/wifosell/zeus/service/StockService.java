package com.wifosell.zeus.service;

import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.model.stock.TransferStockTransaction;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface StockService {
    ImportStockTransaction importStocks(@NonNull Long userId, @Valid ImportStocksRequest request);

    ImportStockTransaction createImportStockTransactionExcel(@NonNull Long userId, @Valid ImportStocksFromExcelRequest request);

    void importStocksFromExcel(@NonNull Long userId, @NonNull Long transactionId);

    Page<ImportStockTransaction> getImportStockTransactions(
            Long userId,
            List<ImportStockTransaction.TYPE> types,
            List<ImportStockTransaction.PROCESSING_STATUS> statuses,
            List<Boolean> isActives,
            Integer offset, Integer limit, String sortBy, String orderBy);

    ImportStockTransaction getImportStockTransaction(Long userId, @NonNull Long importStockTransactionId);

    TransferStockTransaction transferStocks(@NonNull Long userId, @Valid TransferStocksRequest request);

    TransferStockTransaction createTransferStockTransactionExcel(@NonNull Long userId, @Valid TransferStocksFromExcelRequest request);

    void transferStocksFromExcel(@NonNull Long userId, @NonNull Long transactionId);

    Page<TransferStockTransaction> getTransferStockTransactions(
            Long userId,
            List<TransferStockTransaction.TYPE> types,
            List<TransferStockTransaction.PROCESSING_STATUS> statuses,
            List<Boolean> isActives,
            Integer offset, Integer limit, String sortBy, String orderBy);

    TransferStockTransaction getTransferStockTransaction(Long userId, @NonNull Long transferStockTransactionId);
}
