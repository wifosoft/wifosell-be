package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class VariantResponse extends BasicEntityResponse {
    private final String originalCost;
    private final String cost;
    private final String sku;
    private final String barcode;
    private final List<OptionValueResponse> optionValues;
    private final ProductResponse product;
    private final List<StockResponse> stocks;

    public VariantResponse(Variant variant) {
        super(variant);
        this.originalCost = variant.getOriginalCost().toString();
        this.cost = variant.getCost().toString();
        this.sku = variant.getSku();
        this.barcode = variant.getBarcode();
        this.optionValues = variant.getVariantValues().stream()
                .filter(variantValue -> !variantValue.isDeleted())
                .map(VariantValue::getOptionValue)
                .map(OptionValueResponse::new).collect(Collectors.toList());
        this.product = new ProductResponse(variant.getProduct());
        this.stocks = variant.getStocks().stream()
                .filter(stock -> !stock.isDeleted())
                .map(StockResponse::new).collect(Collectors.toList());
    }

    @Getter
    private static class OptionValueResponse extends BasicEntityResponse {
        private final String name;

        public OptionValueResponse(OptionValue optionValue) {
            super(optionValue);
            this.name = optionValue.getName();
        }
    }

    @Getter
    private static class ProductResponse extends BasicEntityResponse {
        private String name;
        private String description;
        private BigDecimal weight;
        private BigDecimal length;
        private BigDecimal width;
        private BigDecimal height;
        private Integer state;
        private Integer status;
        private CategoryResponse category;
        private List<ProductImageResponse> images;
        private List<AttributeResponse> attributes;
        private List<OptionResponse> options;

        public ProductResponse(Product product) {
            super(product);
            this.name = product.getName();
            this.description = product.getDescription();
            this.weight = product.getWeight();
            this.length = product.getLength();
            this.width = product.getWidth();
            this.height = product.getHeight();
            this.state = product.getState();
            this.status = product.getStatus();
            Optional.ofNullable(product.getCategory()).ifPresent(e-> {
                this.category = new CategoryResponse(e);
            });
//            this.category = new CategoryResponse(product.getCategory());
            this.images = product.getImages().stream()
                    .filter(productImage -> !productImage.isDeleted())
                    .map(ProductImageResponse::new).collect(Collectors.toList());
            this.attributes = product.getAttributes().stream()
                    .filter(attribute -> !attribute.isDeleted())
                    .map(AttributeResponse::new).collect(Collectors.toList());
            this.options = product.getOptions().stream()
                    .filter(option -> !option.isDeleted())
                    .map(OptionResponse::new).collect(Collectors.toList());
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
                this.values = option.getOptionValues().stream()
                        .filter(optionValue -> !optionValue.isDeleted())
                        .map(OptionValueResponse::new).collect(Collectors.toList());
            }

            @Getter
            private static class OptionValueResponse extends BasicEntityResponse {
                private final String name;

                public OptionValueResponse(OptionValue optionValue) {
                    super(optionValue);
                    this.name = optionValue.getName();
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
