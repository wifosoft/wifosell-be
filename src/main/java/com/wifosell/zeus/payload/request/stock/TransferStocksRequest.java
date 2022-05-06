package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class TransferStocksRequest {
    @NonNull
    Long fromWarehouseId;

    @NonNull
    Long toWarehouseId;

    @NotEmpty
    List<StockRequest> stocks;
}
