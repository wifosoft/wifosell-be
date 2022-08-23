package com.wifosell.zeus.service.impl.batch_process.warehouse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ImportStockRow {
    public String sku;
    public Integer quantity;
    public BigDecimal unitCost;
}
