package com.wifosell.zeus.payload.provider.lazada.report;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FetchLazadaProductsReport {
    private int fetchTotal;
    private int fetchSuccess;
}
