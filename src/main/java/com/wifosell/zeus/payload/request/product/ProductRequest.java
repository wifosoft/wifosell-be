package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
}
