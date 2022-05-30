package com.wifosell.zeus.service.impl.batch_process.warehouse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferStockRow {
    public String sku;
    public Integer quantity;
}
