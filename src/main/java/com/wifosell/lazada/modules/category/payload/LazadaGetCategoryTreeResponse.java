package com.wifosell.lazada.modules.category.payload;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class LazadaGetCategoryTreeResponse {
    @SerializedName("data")
    List<Category> data;

    @Getter
    public static class Category {
        @SerializedName("children")
        List<Category> children;
        @SerializedName("var")
        boolean var;
        @SerializedName("name")
        String name;
        @SerializedName("leaf")
        boolean leaf;
        @SerializedName("category_id")
        long categoryId;
    }
}
