package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponse extends BasicEntityResponse {
    private final String name;
    private final String description;
    private final Integer weight;
    private final String dimension;
    private final Integer state;
    private final Integer status;
    private final CategoryResponse category;
    private final List<String> images;
    private final List<AttributeResponse> attributes;
    private final List<OptionResponse> options;
    private final List<VariantResponse> variants;

    public ProductResponse(Product product) {
        super(product);
        this.name = product.getName();
        this.description = product.getDescription();
        this.weight = product.getWeight();
        this.dimension = product.getDimension();
        this.state = product.getState();
        this.status = product.getStatus();
        this.category = new CategoryResponse(product.getCategory());
        this.images = product.getImages().stream().map(ProductImage::getUrl).collect(Collectors.toList());
        this.attributes = product.getAttributes().stream().map(AttributeResponse::new).collect(Collectors.toList());
        this.options = product.getOptions().stream().map(OptionResponse::new).collect(Collectors.toList());
        this.variants = product.getVariants().stream().map(VariantResponse::new).collect(Collectors.toList());
    }

    @Getter
    @Setter
    private static class AttributeResponse extends BasicEntityResponse {
        private String name;
        private String value;

        public AttributeResponse(Attribute attribute) {
            super(attribute);
            this.name = attribute.getName();
            this.value = attribute.getValue();
        }
    }

    @Getter
    @Setter
    private static class OptionResponse extends BasicEntityResponse {
        private String name;
        private List<OptionValueResponse> values;

        OptionResponse(OptionModel option) {
            super(option);
            this.name = option.getName();
            this.values = option.getOptionValues().stream().map(OptionValueResponse::new).collect(Collectors.toList());
        }

        @Getter
        @Setter
        private static class OptionValueResponse extends BasicEntityResponse {
            private String value;

            public OptionValueResponse(OptionValue optionValue) {
                super(optionValue);
                this.value = optionValue.getValue();
            }
        }
    }

    @Getter
    @Setter
    private static class VariantResponse extends BasicEntityResponse {
        private Long stock;
        private String cost;
        private String sku;
        private String barcode;
        private List<String> options;

        public VariantResponse(Variant variant) {
            super(variant);
            this.stock = variant.getStock();
            this.cost = variant.getCost().toString();
            this.sku = variant.getSku();
            this.barcode = variant.getBarcode();
            this.options = variant.getVariantValues().stream()
                    .map(VariantValue::getOptionValue)
                    .map(OptionValue::getValue).collect(Collectors.toList());
        }
    }
}
