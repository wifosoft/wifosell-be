package com.wifosell.zeus.payload.response.ecom_sync;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EcomSyncUpdateStockResponse {
    private int lazadaTotal;
    private int lazadaSuccess;
    private int sendoTotal;
    private int sendoSuccess;
    private int offlineTotal;
}
