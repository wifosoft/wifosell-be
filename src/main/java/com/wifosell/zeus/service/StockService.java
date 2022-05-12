package com.wifosell.zeus.service;

import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import lombok.NonNull;

import javax.validation.Valid;

public interface StockService {
    void importStocks(@NonNull Long userId, @Valid ImportStocksRequest request);

    void importStocksFromExcel(@NonNull Long userId, @Valid ImportStocksFromExcelRequest request);

    void transferStocks(@NonNull Long userId, @Valid TransferStocksRequest request);
}
