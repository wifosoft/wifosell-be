package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class ProductRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String sku;

    @NotBlank
    @Size(max = 255)
    private String barcode;

    @NotNull
    private Long categoryId;

    @NotNull
    private Integer weight;

    @NotBlank
    @Size(max = 50)
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

    @Getter
    @Setter
    public static class AttributeRequest {
        @NotBlank
        @Size(max = 255)
        private String name;

        @NotBlank
        @Size(max = 255)
        private String value;
    }

    @Getter
    @Setter
    public static class OptionRequest {
        @NotBlank
        @Size(max = 255)
        private String name;

        @NotEmpty
        private List<String> values;
    }

    @Getter
    @Setter
    public static class VariantRequest {
        @NonNull
        @PositiveOrZero
        private Long stock;

        @NotBlank
        @Size(max = 255)
        private String sku;

        @NotBlank
        @Size(max = 255)
        private String cost;
    }
}
