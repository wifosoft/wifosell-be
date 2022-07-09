package com.wifosell.zeus.model.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopOrderNumRegion {
    private String country;
    private String city;
    private Long number;
}
