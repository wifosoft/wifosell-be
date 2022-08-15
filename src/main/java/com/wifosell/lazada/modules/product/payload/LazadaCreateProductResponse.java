package com.wifosell.lazada.modules.product.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class LazadaCreateProductResponse {
    public static LazadaCreateProductResponse fromJson(String json) {
        return new Gson().fromJson(json, LazadaCreateProductResponse.class);
    }

    @SerializedName("data")
    private Data data;

    @Getter
    public static class Data {
        @SerializedName("item_id")
        private Long itemId;

        @SerializedName("sku_list")
        private List<Sku> skus;

        @Getter
        public static class Sku {
            @SerializedName("shop_sku")
            private String shopSku;

            @SerializedName("seller_sku")
            private String sellerSku;

            @SerializedName("sku_id")
            private Long skuId;
        }
    }
}
