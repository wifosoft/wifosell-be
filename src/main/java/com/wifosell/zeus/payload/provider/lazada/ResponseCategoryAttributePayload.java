package com.wifosell.zeus.payload.provider.lazada;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseCategoryAttributePayload {
    @Getter
    @Setter
    public class CategoryAttributeItem {

        @Getter
        @Setter
        public class AdvancedItem {
            @SerializedName("is_key_prop")
            @JsonProperty("is_key_prop")
            private Integer isKeyProp;
        }

        @Getter
        @Setter
        public class Options {
            private String name;
            private String en_name;
            private Long id;
        }

        private List<Options> options;
        private AdvancedItem advanced;

        private Long id;

        private String label;

        @SerializedName("is_sale_prop")
        @JsonProperty("is_sale_prop")
        private Integer isSaleProp;

        private String name;


        @SerializedName("input_type")
        @JsonProperty("input_type")
        private String inputType;

        @SerializedName("attribute_type")
        @JsonProperty("attribute_type")
        private String attributeType;


        @SerializedName("is_mandatory")
        @JsonProperty("is_mandatory")
        private Long isMandatory;


    }

    public List<CategoryAttributeItem> data;
    public Integer code;
    public String requestId;

}
