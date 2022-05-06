package com.wifosell.zeus.payload.request.stock;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ImportStocksFromExcelRequest {
    @NotBlank
    String url;
}
