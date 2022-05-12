package com.wifosell.zeus.payload.request.statistic;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StatisticRequest {

    @NotBlank
    @Size(max = 255)
    private Long dateFrom;

    @NotBlank
    @Size(max = 255)
    private Long dateTo;

}

