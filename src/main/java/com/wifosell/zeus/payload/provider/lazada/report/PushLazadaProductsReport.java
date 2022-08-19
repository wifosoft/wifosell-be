package com.wifosell.zeus.payload.provider.lazada.report;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PushLazadaProductsReport {
    private int pushTotal;
    private int pushSuccess;
    private int createTotal;
    private int createSuccess;
    private int updateTotal;
    private int updateSuccess;
    private int fetchSuccess;
}
