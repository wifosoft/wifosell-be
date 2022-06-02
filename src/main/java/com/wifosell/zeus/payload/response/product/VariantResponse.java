package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class VariantResponse extends BasicEntityResponse {
    private final String cost;
    private final String sku;
    private final String barcode;
    private final List<OptionValueResponse> options;
    private final ProductResponse product;
    private final List<StockResponse> stocks;

    public VariantResponse(Variant variant) {
        super(variant);
        this.cost = variant.getCost().toString();
        this.sku = variant.getSku();
        this.barcode = variant.getBarcode();
        this.options = variant.getVariantValues().stream()
                .map(VariantValue::getOptionValue)
                .map(OptionValueResponse::new).collect(Collectors.toList());
        this.product = new ProductResponse(variant.getProduct());
        this.stocks = variant.getStocks().stream().map(StockResponse::new).collect(Collectors.toList());
    }

    @Getter
    private static class OptionValueResponse extends BasicEntityResponse {
        private final String value;

        public OptionValueResponse(OptionValue optionValue) {
            super(optionValue);
            this.value = optionValue.getName();
        }
    }

    @Getter
    private static class ProductResponse extends BasicEntityResponse {
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
        }

        @Getter
        private static class AttributeResponse extends BasicEntityResponse {
            private final String name;
            private final String value;

            public AttributeResponse(Attribute attribute) {
                super(attribute);
                this.name = attribute.getName();
                this.value = attribute.getValue();
            }
        }

        @Getter
        private static class OptionResponse extends BasicEntityResponse {
            private final String name;
            private final List<OptionValueResponse> values;

            OptionResponse(OptionModel option) {
                super(option);
                this.name = option.getName();
                this.values = option.getOptionValues().stream().map(OptionValueResponse::new).collect(Collectors.toList());
            }

            @Getter
            private static class OptionValueResponse extends BasicEntityResponse {
                private final String value;

                public OptionValueResponse(OptionValue optionValue) {
                    super(optionValue);
                    this.value = optionValue.getName();
                }
            }
        }
    }

    @Getter
    private static class StockResponse extends BasicEntityResponse {
        private final Warehouse warehouse;
        private final Integer actualQuantity;
        private final Integer quantity;

        public StockResponse(Stock stock) {
            super(stock);
            this.warehouse = stock.getWarehouse();
            this.actualQuantity = stock.getActualQuantity();
            this.quantity = stock.getQuantity();
        }
    }
}
