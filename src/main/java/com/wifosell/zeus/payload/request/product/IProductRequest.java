package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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

    List<String> getImages();

    List<AttributeRequest> getAttributes();

    List<OptionRequest> getOptions();

    List<VariantRequest> getVariants();

    Boolean getIsActive();

    @Getter
    @Setter
    class AttributeRequest {
        Long id;

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
        Long id;

        @NotBlank
        @Size(max = 100)
        String name;

        @NotEmpty
        List<OptionValueRequest> values;
    }

    @Getter
    @Setter
    class OptionValueRequest {
        Long id;

        @NotBlank
        String name;
    }

    @Getter
    @Setter
    class VariantRequest {
        Long id;

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
