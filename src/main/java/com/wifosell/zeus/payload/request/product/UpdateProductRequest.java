package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateProductRequest implements IProductRequest {
    private String name;

    private String description;

    private Long categoryId;

    private Integer weight;

    private String dimension;

    private Integer state;

    private Integer status;

    private List<ImageRequest> images;

    private List<AttributeRequest> attributes;

    private List<OptionRequest> options;

    private List<VariantRequest> variants;

    private Boolean isActive;
}
