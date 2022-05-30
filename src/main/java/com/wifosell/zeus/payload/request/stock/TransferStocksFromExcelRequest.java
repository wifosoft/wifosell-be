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
public class TransferStocksFromExcelRequest {
    @NotNull
    Long fromWarehouseId;

    @NonNull
    Long toWarehouseId;

    @NotEmpty
    String source;
}
