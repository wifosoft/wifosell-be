package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Attr;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponse extends BasicEntityResponse {
    private final String name;
    private final String sku;
    private final String barcode;
    private final Integer weight;
    private final String dimension;
    private final Integer state;
    private final Integer status;
    private final List<AttributeResponse> attributes;
    private final List<OptionResponse> options;
    private final List<Variant> variants;

    public ProductResponse(Product product) {
        super(product);
        this.name = product.getName();
        this.sku = product.getSku();
        this.barcode = product.getBarcode();
        this.weight = product.getWeight();
        this.dimension = product.getDimension();
        this.state = product.getState();
        this.status = product.getStatus();
        this.attributes = product.getAttributes().stream().map(AttributeResponse::new).collect(Collectors.toList());
        this.options = product.getOptions().stream().map(OptionResponse::new).collect(Collectors.toList());
        this.variants = product.getVariants();
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
}
