package com.wifosell.zeus.payload.provider.lazada;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseCategoryTreePayload {
    @Getter
    @Setter
    public static class CategoryTreeItem {
        boolean var;
        String name;
        boolean leaf;

        @SerializedName("category_id")
        @JsonProperty("category_id")
        Long categoryId;

        @SerializedName("children")
        @JsonProperty("children")
        List<CategoryTreeItem> children;
    }

    public List<CategoryTreeItem> data;
    public Integer code;
    public String requestId;

}
