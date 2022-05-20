package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class ImportStocksFromExcelRequest {
    @NotNull
    Long warehouseId;

    @NonNull
    Long supplierId;

    @NotEmpty
    String excelFile;


}
