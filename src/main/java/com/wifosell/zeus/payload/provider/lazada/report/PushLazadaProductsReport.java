package com.wifosell.zeus.payload.provider.lazada.report;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PushLazadaProductsReport {
    private int pushTotal;
    private int pushSuccess;
    private int fetchSuccess;
}
