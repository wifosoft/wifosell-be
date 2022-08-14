package com.wifosell.lazada.modules.product.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class LazadaGetProductsResponse {
    public static LazadaGetProductsResponse fromJson(String json) {
        return new Gson().fromJson(json, LazadaGetProductsResponse.class);
    }

    @SerializedName("data")
    private Data data;

    @Getter
    public static class Data {
        @SerializedName("total_products")
        private int totalProducts;

        @SerializedName("products")
        private List<Product> products;

        @Getter
        public static class Product {
            @SerializedName("item_id")
            private Long itemId;
        }
    }
}
