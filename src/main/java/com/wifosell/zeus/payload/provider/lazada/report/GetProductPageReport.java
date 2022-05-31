package com.wifosell.zeus.payload.provider.lazada.report;

import com.wifosell.zeus.payload.provider.lazada.ResponseListProductPayload;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductPageReport extends GetAllProductReport{

    ResponseListProductPayload responseListProductPayload;

    public GetProductPageReport(int totalProduct, int totalSku) {
        super(totalProduct, totalSku);
    }
}
