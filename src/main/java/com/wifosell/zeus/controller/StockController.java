package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/import")
    public ResponseEntity<GApiResponse<ImportStockTransaction>> importStocks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ImportStocksRequest request
    ) {
        ImportStockTransaction transaction = stockService.importStocks(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(transaction));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/importFromExcel")
    public ResponseEntity<GApiResponse<Boolean>> importStocksFromExcel(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ImportStocksFromExcelRequest request
    ) {
        stockService.importStocksFromExcel(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/import/transactions")
    public ResponseEntity<GApiResponse<Page<ImportStockTransaction>>> getImportStockTransactions(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "type", required = false) List<ImportStockTransaction.TYPE> types,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<ImportStockTransaction> transactions = stockService.getImportStockTransactions(
                userPrincipal.getId(), types, isActives, offset, limit, sortBy, orderBy);
        return ResponseEntity.ok(GApiResponse.success(transactions));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/import/transactions/{importTransactionId}")
    public ResponseEntity<GApiResponse<ImportStockTransaction>> getImportStockTransaction(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "importTransactionId") Long importTransactionId
    ) {
        ImportStockTransaction transaction = stockService.getImportStockTransaction(
                userPrincipal.getId(), importTransactionId);
        return ResponseEntity.ok(GApiResponse.success(transaction));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/transfer")
    public ResponseEntity<GApiResponse<Boolean>> transferStocks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody TransferStocksRequest request
    ) {
        stockService.transferStocks(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/transfer/transactions")
    public ResponseEntity<GApiResponse<Boolean>> getTransferStockTransactions(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        // TODO haukc
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/transfer/transactions/{transferTransactionId}")
    public ResponseEntity<GApiResponse<Boolean>> getTransferStockTransactions(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "transferTransactionId") Long transferTransactionId
    ) {
        // TODO haukc
        return ResponseEntity.ok(GApiResponse.success(true));
    }
}
