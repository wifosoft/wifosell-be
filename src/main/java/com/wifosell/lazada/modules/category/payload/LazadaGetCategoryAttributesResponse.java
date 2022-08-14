package com.wifosell.lazada.modules.category.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
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

        @Getter(value = AccessLevel.NONE)
        @SerializedName("options")
        private List<Option> _options;

        private transient Map<String, String> options;  // {name, enName}

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
            attribute._options = null;
            data.put(attribute.getName(), attribute);
        });
        _data = null;
    }
}
