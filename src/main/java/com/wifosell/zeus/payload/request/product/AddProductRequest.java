package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class AddProductRequest implements IProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long categoryId;

    private Integer weight;

    private String dimension;

    private Integer state;

    private Integer status;

    private List<ImageRequest> images;

    private List<AttributeRequest> attributes;

    private List<OptionRequest> options;

    @NonNull
    private List<VariantRequest> variants;

    private Boolean isActive;
}
