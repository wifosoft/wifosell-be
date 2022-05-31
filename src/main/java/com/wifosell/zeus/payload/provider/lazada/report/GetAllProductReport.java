package com.wifosell.zeus.payload.provider.lazada.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetAllProductReport {
    private int totalProduct;
    private int totalSku;
}
