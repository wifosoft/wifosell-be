package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.model.stock.TransferStockTransaction;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockService {
    ImportStockTransaction importStocks(Long userId, ImportStocksRequest request);

    void importStock(Long userId, Warehouse warehouse, Variant variant, Integer actualQuantity, Integer quantity);

    void updateSystemStock(Warehouse warehouse, Variant variant, Integer actualQuantity, Integer quantity);

    ImportStockTransaction createImportStockTransactionExcel(Long userId, ImportStocksFromExcelRequest request);

    void importStocksFromExcel(Long userId, Long transactionId);

    Page<ImportStockTransaction> getImportStockTransactions(
            Long userId,
            List<ImportStockTransaction.TYPE> types,
            List<ImportStockTransaction.PROCESSING_STATUS> statuses,
            List<Boolean> isActives,
            Integer offset, Integer limit, String sortBy, String orderBy);

    ImportStockTransaction getImportStockTransaction(Long userId, Long importStockTransactionId);

    TransferStockTransaction transferStocks(Long userId, TransferStocksRequest request);

    TransferStockTransaction createTransferStockTransactionExcel(Long userId, TransferStocksFromExcelRequest request);

    void transferStocksFromExcel(Long userId, Long transactionId);

    Page<TransferStockTransaction> getTransferStockTransactions(
            Long userId,
            List<TransferStockTransaction.TYPE> types,
            List<TransferStockTransaction.PROCESSING_STATUS> statuses,
            List<Boolean> isActives,
            Integer offset, Integer limit, String sortBy, String orderBy);

    TransferStockTransaction getTransferStockTransaction(Long userId, Long transferStockTransactionId);
}
