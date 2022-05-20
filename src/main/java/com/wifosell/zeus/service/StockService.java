package com.wifosell.zeus.service;

import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import java.util.List;

public interface StockService {
    ImportStockTransaction importStocks(@NonNull Long userId, @Valid ImportStocksRequest request);

    ImportStockTransaction importStocksFromExcel(@NonNull Long userId, @Valid ImportStocksFromExcelRequest request);

    Page<ImportStockTransaction> getImportStockTransactions(Long userId, List<ImportStockTransaction.TYPE> types, List<Boolean> isActives,
                                                            Integer offset, Integer limit, String sortBy, String orderBy);

    ImportStockTransaction getImportStockTransaction(Long userId, @NonNull Long importStockTransactionId);

    void transferStocks(@NonNull Long userId, @Valid TransferStocksRequest request);
}
