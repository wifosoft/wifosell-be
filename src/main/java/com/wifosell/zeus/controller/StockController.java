package com.wifosell.zeus.controller;

import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/import")
    public ResponseEntity<GApiResponse<Boolean>> importStocks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ImportStocksRequest request
    ) {
        stockService.importStocks(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(true));
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
    @GetMapping("/import/transaction/{importTransactionId}")
    public ResponseEntity<GApiResponse<Boolean>> getStockHistory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "importTransactionId") Long importTransactionId
    ) {
        // TODO haukc
        return ResponseEntity.ok(GApiResponse.success(true));
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
}
