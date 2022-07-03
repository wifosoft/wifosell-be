package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.attribute.AttributeResponse;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import com.wifosell.zeus.payload.response.option.OptionResponse;
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
    private final List<ProductImageResponse> images;
    private final List<AttributeResponse> attributes;
    private final List<OptionResponse> options;
    private final List<VariantResponse> variants;

    public ProductResponse(Product product) {
        this(product, null, null, null, null);
    }

    public ProductResponse(Product product, String keyword, List<Long> warehouseIds, Integer minQuantity, Integer maxQuantity) {
        super(product);
        this.name = product.getName();
        this.description = product.getDescription();
        this.weight = product.getWeight();
        this.dimension = product.getDimension();
        this.state = product.getState();
        this.status = product.getStatus();
        this.category = new CategoryResponse(product.getCategory());
        this.images = product.getImages().stream()
                .filter(productImage -> !productImage.isDeleted())
                .map(ProductImageResponse::new).collect(Collectors.toList());
        this.attributes = product.getAttributes().stream()
                .filter(attribute -> !attribute.isDeleted())
                .map(AttributeResponse::new).collect(Collectors.toList());
        this.options = product.getOptions().stream()
                .filter(option -> !option.isDeleted())
                .map(OptionResponse::new).collect(Collectors.toList());

        List<Variant> variants = product.getVariants().stream()
                .filter(variant -> !variant.isDeleted()).collect(Collectors.toList());

        if (warehouseIds != null || minQuantity != null || maxQuantity != null) {
            variants = variants.stream().filter(variant -> {
                List<Stock> stocks = variant.getStocks().stream().filter(stock -> {
                    boolean inWarehouse = warehouseIds == null || warehouseIds.contains(stock.getWarehouse().getId());
                    boolean betweenQuantity = (minQuantity == null || stock.getQuantity() >= minQuantity)
                            && (maxQuantity == null || stock.getQuantity() <= maxQuantity);
                    return inWarehouse && betweenQuantity;
                }).collect(Collectors.toList());
                variant.setStocks(stocks);
                return !stocks.isEmpty();
            }).collect(Collectors.toList());
        }

        if (keyword != null && !keyword.isEmpty()) {
            List<Variant> variants1 = variants.stream()
                    .filter(variant -> variant.getSku().equals(keyword))
                    .collect(Collectors.toList());
            if (!variants1.isEmpty()) {
                variants = variants1;
            }
        }

        this.variants = variants.stream().map(VariantResponse::new).collect(Collectors.toList());
    }

    @Getter
    @Setter
    private static class VariantResponse extends BasicEntityResponse {
        private final String originalCost;
        private final String cost;
        private final String sku;
        private final String barcode;
        private final List<OptionValueResponse> optionValues;
        private final List<StockResponse> stocks;

        public VariantResponse(Variant variant) {
            super(variant);
            this.originalCost = variant.getOriginalCost().toString();
            this.cost = variant.getCost().toString();
            this.sku = variant.getSku();
            this.barcode = variant.getBarcode();
            this.optionValues = variant.getVariantValues().stream()
                    .map(VariantValue::getOptionValue)
                    .map(OptionValueResponse::new).collect(Collectors.toList());
            this.stocks = variant.getStocks().stream()
                    .filter(stock -> !stock.getVariant().isDeleted())
                    .map(StockResponse::new)
                    .collect(Collectors.toList());
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
}
