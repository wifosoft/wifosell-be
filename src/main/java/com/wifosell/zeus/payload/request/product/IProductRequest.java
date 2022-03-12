package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

public interface IProductRequest {
    @Size(max = 200)
    String getName();

    @Size(max = 100)
    String getDescription();

    Long getCategoryId();

    @Positive
    Integer getWeight();

    @Size(max = 50)
    String getDimension();

    Integer getState();

    Integer getStatus();

    List<AttributeRequest> getAttributes();

    List<OptionRequest> getOptions();

    List<VariantRequest> getVariants();

    Boolean getActive();

    @Getter
    @Setter
    class AttributeRequest {
        @NotBlank
        @Size(max = 100)
        String name;

        @NotBlank
        @Size(max = 100)
        String value;
    }

    @Getter
    @Setter
    class OptionRequest {
        @NotBlank
        @Size(max = 100)
        String name;

        @NotEmpty
        List<String> values;
    }

    @Getter
    @Setter
    class VariantRequest {
        @PositiveOrZero
        Long stock;

        @NotBlank
        @Size(max = 50)
        String cost;

        @NotBlank
        @Size(max = 100)
        String sku;

        @Size(max = 100)
        String barcode;
    }
}
