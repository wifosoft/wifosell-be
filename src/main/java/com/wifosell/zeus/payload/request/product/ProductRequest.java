package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String sku;

    @Size(max = 255)
    private String barcode;

    private Long categoryId;

    private Integer weight;

    @Size(max = 50)
    private String dimension;

    private Integer state;

    private Integer status;

    private Integer stock;
}
