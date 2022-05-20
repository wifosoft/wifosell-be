package com.wifosell.zeus.payload.request.stock;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ImportStocksFormExcelMultipleFormRequest {
    Long warehouseId;
    Long supplierId;
    private MultipartFile file;
}
