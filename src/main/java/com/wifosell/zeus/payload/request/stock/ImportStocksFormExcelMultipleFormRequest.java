package com.wifosell.zeus.payload.request.stock;

import org.springframework.web.multipart.MultipartFile;

public class ImportStocksFormExcelMultipleFormRequest {
    Long warehouseId;
    Long supplierId;
    private MultipartFile file;
}
