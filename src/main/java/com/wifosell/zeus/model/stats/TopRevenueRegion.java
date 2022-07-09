package com.wifosell.zeus.model.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TopRevenueRegion {
    private String country;
    private String city;
    private BigDecimal revenue;
}
