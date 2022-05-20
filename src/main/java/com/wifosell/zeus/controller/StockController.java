package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import com.wifosell.zeus.payload.response.stock.ImportStockTransactionResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.StockService;
import com.wifosell.zeus.service.impl.storage.FileSystemStorageService;
import com.wifosell.zeus.service.impl.storage.UploadFileResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @Autowired
    private FileSystemStorageService fileStorageService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/import")
    public ResponseEntity<GApiResponse<ImportStockTransactionResponse>> importStocks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ImportStocksRequest request
    ) {
        ImportStockTransaction transaction = stockService.importStocks(userPrincipal.getId(), request);
        ImportStockTransactionResponse response = new ImportStockTransactionResponse(transaction);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createImportStockTransactionExcel")
    public ResponseEntity<GApiResponse<ImportStockTransaction>> createImportStockTransactionExcel(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ImportStocksFromExcelRequest request
    ) {
        ImportStockTransaction transaction = stockService.createImportStockTransactionExcel(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(transaction));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/importStockTransactionExcelFormData",  consumes = {"multipart/form-data"},  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GApiResponse<ImportStockTransaction>> importStockTransactionExcelFormData(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam ("warehouseId") @NotNull Long warehouseId,
            @RequestParam ("supplierId")  @NonNull Long supplierId,
            @RequestParam(value = "file", required = true) MultipartFile file
    ) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/upload/downloadFile/")
                .path(fileName)
                .toUriString();

        UploadFileResponse uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());


        ImportStocksFromExcelRequest importStocksFromExcelRequest = new ImportStocksFromExcelRequest();
        importStocksFromExcelRequest.setWarehouseId(warehouseId);
        importStocksFromExcelRequest.setSupplierId(supplierId);
        importStocksFromExcelRequest.setExcelFile(uploadFileResponse.getFileName());

        ImportStockTransaction transaction = stockService.createImportStockTransactionExcel(userPrincipal.getId(), importStocksFromExcelRequest);
        return ResponseEntity.ok(GApiResponse.success(transaction));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/import/transactions")
    public ResponseEntity<GApiResponse<Page<ImportStockTransactionResponse>>> getImportStockTransactions(
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
        Page<ImportStockTransactionResponse> responses = transactions.map(ImportStockTransactionResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/import/transactions/{importTransactionId}")
    public ResponseEntity<GApiResponse<ImportStockTransactionResponse>> getImportStockTransaction(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "importTransactionId") Long importTransactionId
    ) {
        ImportStockTransaction transaction = stockService.getImportStockTransaction(
                userPrincipal.getId(), importTransactionId);
        ImportStockTransactionResponse response = new ImportStockTransactionResponse(transaction);
        return ResponseEntity.ok(GApiResponse.success(response));
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
