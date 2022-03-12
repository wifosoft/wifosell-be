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

    @NotBlank
    private String description;

    @NotBlank
    private String barcode;

    @NotNull
    private Long categoryId;

    @NotNull
    private Integer weight;

    @NotBlank
    private String dimension;

    @NotNull
    private Integer state;

    @NotNull
    private Integer status;

    @NotNull
    private List<AttributeRequest> attributes;

    @NotNull
    private List<OptionRequest> options;

    @NonNull
    private List<VariantRequest> variants;

    private Boolean active;
}
