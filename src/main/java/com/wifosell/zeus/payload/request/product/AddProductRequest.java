package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AddProductRequest implements IProductRequest {
    @NotBlank
    private String name;

    private String description;

    private Long categoryId;

    private BigDecimal weight;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private Integer state;

    private Integer status;

    private List<ImageRequest> images;

    private List<AttributeRequest> attributes;

    private List<OptionRequest> options;


    private List<VariantRequest> variants;

    private Boolean isActive;
}
