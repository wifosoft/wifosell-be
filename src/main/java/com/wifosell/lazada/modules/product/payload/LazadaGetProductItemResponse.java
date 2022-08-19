package com.wifosell.lazada.modules.product.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.wifosell.lazada.modules.category.payload.LazadaGetCategoryAttributesResponse;
import lombok.AccessLevel;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class LazadaGetProductItemResponse {
    public static LazadaGetProductItemResponse fromJson(String json) {
        LazadaGetProductItemResponse object = new Gson().fromJson(json, LazadaGetProductItemResponse.class);
        object.processRawData();
        return object;
    }

    public void translateData(LazadaGetCategoryAttributesResponse res) {
        // Attributes
        Map<String, String> translatedAttributes = new HashMap<>();

        data.attributes.forEach((rawName, rawValue) -> {
            LazadaGetCategoryAttributesResponse.Attribute attributeRes = res.getData().get(rawName);

            if (attributeRes != null) {
                String translatedName = attributeRes.getLabel();
                if (translatedName == null) translatedName = rawName;

                String translatedValue = attributeRes.getOptions().get(rawValue);
                if (translatedValue == null) translatedValue = rawValue;

                translatedAttributes.put(translatedName, translatedValue);
            } else {
                translatedAttributes.put(rawName, rawValue);
            }
        });

        data.attributes = translatedAttributes;

        // Variations
        data.variations.forEach(variation -> {
            LazadaGetCategoryAttributesResponse.Attribute attributeRes = res.getData().get(variation.name);

            if (attributeRes != null) {
                String translatedName = attributeRes.getLabel();
                if (translatedName == null) translatedName = variation.name;
                variation.name = translatedName;

                variation.options = variation.options.stream().map(option -> {
                    String translatedOption = attributeRes.getOptions().get(option);
                    if (translatedOption == null) translatedOption = option;
                    return translatedOption;
                }).collect(Collectors.toList());
            }
        });

        // Skus
        data.skus.forEach(sku -> {
            Map<String, String> translatedOptions = new HashMap<>();

            sku.options.forEach((rawName, rawValue) -> {
                LazadaGetCategoryAttributesResponse.Attribute attributeRes = res.getData().get(rawName);

                if (attributeRes != null) {
                    String translatedName = attributeRes.getLabel();
                    if (translatedName == null) translatedName = rawName;

                    String translatedValue = attributeRes.getOptions().get(rawValue);
                    if (translatedValue == null) translatedValue = rawValue;

                    translatedOptions.put(translatedName, translatedValue);
                } else {
                    translatedOptions.put(rawName, rawValue);
                }
            });

            sku.options = translatedOptions;
        });
    }

    @SerializedName("data")
    private Data data;

    @Getter
    public static class Data {
        @SerializedName("created_time")
        private String createdTime;

        @SerializedName("updated_time")
        private String updatedTime;

        @SerializedName("images")
        private List<String> images;

        @Getter(value = AccessLevel.NONE)
        @SerializedName("skus")
        private List<Map<String, Object>> _skus;

        private transient List<Sku> skus;

        @SerializedName("item_id")
        private Long itemId;

        @Getter(value = AccessLevel.NONE)
        @SerializedName("variation")
        private Map<String, Variation> _variations;

        private List<Variation> variations;

        @SerializedName("primary_category")
        private Long primaryCategoryId;

        @SerializedName("attributes")
        private Map<String, String> attributes;

        @SerializedName("status")
        private String status;

        private transient String name;

        private transient String description;

        public boolean isActive() {
            return "Active".equals(status);
        }

        @Getter
        public static class Variation {
            @SerializedName("name")
            private String name;

            @SerializedName("hasImage")
            private boolean hasImage;

            @SerializedName("customize")
            private boolean customize;

            @SerializedName("options")
            private List<String> options;

            @SerializedName("label")
            private String label;
        }

        @Getter
        public static class Sku {
            @SerializedName("SkuId")
            private Long skuId;

            @SerializedName("SellerSku")
            private String sellerSku;

            @SerializedName("ShopSku")
            private String shopSku;

            @SerializedName("quantity")
            private int quantity;

            @SerializedName("Available")
            private int available;

            @SerializedName("special_price")
            private BigDecimal specialPrice;

            @SerializedName("price")
            private BigDecimal price;

            @SerializedName("Images")
            private List<String> images;

            @SerializedName("package_weight")
            private BigDecimal packageWeight;

            @SerializedName("package_width")
            private BigDecimal packageWidth;

            @SerializedName("package_height")
            private BigDecimal packageHeight;

            @SerializedName("package_length")
            private BigDecimal packageLength;

            @SerializedName("Url")
            private String url;

            @SerializedName("Status")
            private String status;

            private transient List<Integer> optionIndices;

            private transient Map<String, String> options;

            public boolean isActive() {
                return "active".equals(status);
            }
        }
    }

    private void processRawData() {
        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();

        data.name = data.attributes.remove("name");
        data.description = data.attributes.remove("description");

        data.variations = new ArrayList<>(data._variations.values());

        data.skus = new ArrayList<>();
        data._skus.forEach(rawSku -> {
            try {
                Data.Sku sku = gson.fromJson(objectMapper.writeValueAsString(rawSku), Data.Sku.class);
                sku.options = new HashMap<>();
                data.variations.forEach(variation -> {
                    sku.options.put(variation.name, String.valueOf(rawSku.get(variation.name)));
                });
                data.skus.add(sku);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        data._variations = null;
        data._skus = null;
    }
}
