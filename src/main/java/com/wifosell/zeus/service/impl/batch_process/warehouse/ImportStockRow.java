package com.wifosell.zeus.service.impl.batch_process.warehouse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImportStockRow {
    public String sku;
    public Integer quantity;
    public Integer unitCost;
}
