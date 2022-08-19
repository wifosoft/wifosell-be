package com.wifosell.lazada.modules.category.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.wifosell.lazada.modules.product.payload.LazadaCreateProductRequest;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LazadaGetCategoryAttributesResponse {

    public static LazadaGetCategoryAttributesResponse fromJson(String json) {
        LazadaGetCategoryAttributesResponse object = new Gson().fromJson(json, LazadaGetCategoryAttributesResponse.class);
        object.processRawData();
        return object;
    }

    @Getter(value = AccessLevel.NONE)
    @SerializedName("data")
    private List<Attribute> _data;

    private transient Map<String, Attribute> data;  // {name, attribute}

    @Getter
    public static class Attribute {
        @SerializedName("name")
        private String name;

        @SerializedName("label")
        private String label;

        @SerializedName("is_mandatory")
        private byte isMandatory;

        @Getter(value = AccessLevel.NONE)
        @SerializedName("options")
        private List<Option> _options;

        private transient Map<String, String> options;  // {enName, name}

        @Getter
        public static class Option {
            @SerializedName("name")
            private String name;

            @SerializedName("en_name")
            private String enName;
        }
    }

    private void processRawData() {
        data = new HashMap<>();
        _data.forEach(attribute -> {
            attribute.options = new HashMap<>();
            attribute._options.forEach(option -> attribute.options.put(option.getEnName(), option.getName()));
//            attribute._options = null;
            data.put(attribute.getName(), attribute);
        });
//        _data = null;
    }

    private Map<String, String> getMandatoryAttributes() {
        Map<String, String> mandatoryAttributes = new HashMap<>();
        data.values().forEach(attribute -> {
            if (attribute.isMandatory == 1) {
                String key = attribute.name;
                String value = attribute._options == null || attribute._options.isEmpty() ? "" : attribute._options.get(0).enName;
                mandatoryAttributes.put(key, value);
            }
        });
        return mandatoryAttributes;
    }

    public Map<String, String> getRemainingMandatoryAttributes() {
        List<String> attributeKeys = List.of(
                LazadaCreateProductRequest.Attribute.NAME,
                LazadaCreateProductRequest.Attribute.DESCRIPTION,
                LazadaCreateProductRequest.Attribute.BRAND,
                LazadaCreateProductRequest.Sku.SELLER_SKU,
                LazadaCreateProductRequest.Sku.QUANTITY,
                LazadaCreateProductRequest.Sku.PRICE,
                LazadaCreateProductRequest.Sku.SPECIAL_PRICE,
                LazadaCreateProductRequest.Sku.SPECIAL_FROM_DATE,
                LazadaCreateProductRequest.Sku.SPECIAL_TO_DATE,
                LazadaCreateProductRequest.Sku.PACKAGE_HEIGHT,
                LazadaCreateProductRequest.Sku.PACKAGE_LENGTH,
                LazadaCreateProductRequest.Sku.PACKAGE_WIDTH,
                LazadaCreateProductRequest.Sku.PACKAGE_WEIGHT,
                LazadaCreateProductRequest.Sku.PACKAGE_WEIGHT,
                LazadaCreateProductRequest.Sku.PACKAGE_CONTENT,
                LazadaCreateProductRequest.Sku.IMAGES
        );
        Map<String, String> attributes = getMandatoryAttributes();
        for (String attributeKey : attributeKeys) {
            attributes.remove(attributeKey);
        }
        return attributes;
    }
}
