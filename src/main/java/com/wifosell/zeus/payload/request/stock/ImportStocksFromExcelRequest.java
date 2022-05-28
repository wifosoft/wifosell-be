package com.wifosell.zeus.payload.request.stock;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ImportStocksFromExcelRequest {
    @NotNull
    Long warehouseId;

    @NonNull
    Long supplierId;

    @NotEmpty
    String source;
}
