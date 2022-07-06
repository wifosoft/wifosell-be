package com.wifosell.zeus.model.stats;

import com.wifosell.zeus.model.product.Variant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TopProfitVariant {
    private Variant variant;
    private BigDecimal revenue;
}
