package com.wifosell.zeus.payload.request.product;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public interface IProductRequest {
    String getName();

    String getDescription();

    Long getCategoryId();

    BigDecimal getWeight();

    BigDecimal getLength();

    BigDecimal getWidth();

    BigDecimal getHeight();

    Integer getState();

    Integer getStatus();

    List<ImageRequest> getImages();

    List<AttributeRequest> getAttributes();

    List<OptionRequest> getOptions();

    List<VariantRequest> getVariants();

    Boolean getIsActive();

    @Getter
    @Setter
    class ImageRequest {

        public ImageRequest() {}
        public ImageRequest(Long _id, String _url) {
            this.id = _id;
            this.url = _url;
        }
        Long id;

        @NotBlank
        String url;
    }


    @Getter
    @Setter
    class AttributeRequest {
        Long id;

        @NotBlank
        String name;

        @NotBlank
        String value;
    }

    @Getter
    @Setter
    class OptionRequest {
        Long id;

        @NotBlank
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
        String originalCost;

        @NotBlank
        String cost;

        @NotBlank
        String sku;

        String barcode;

        Boolean isActive;
    }
}
