package com.wifosell.zeus.payload.provider.lazada;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ResponseListProductPayload {
    @Getter
    @Setter
    public class Attributes {
        public String name;
        public String description;
        public String brand;
        //public String clothing_material;
        // public String source;
    }

    @Getter
    @Setter
    public class Data {
        public int total_products;
        public ArrayList<Product> products;
    }

    @Getter
    @Setter
    public class MultiWarehouseInventory {
        public int occupyQuantity;
        public int quantity;
        public int totalQuantity;
        public int withholdQuantity;
        public String warehouseCode;
        public int sellableQuantity;
    }

    @Getter
    public class Product {
        public boolean trialProduct;
        public String created_time;
        public String updated_time;
        public ArrayList<String> images;
        public ArrayList<Sku> skus;
        public Long item_id;
        public Long primary_category;
        public ArrayList<Object> marketImages;
        public Attributes attributes;
        public String status;
    }

    @Getter

    public class Sku {
        @SerializedName("Status")
        @JsonProperty("Status")
        public String status;
        public int quantity;
        public int sellableStock;
        @SerializedName("Images")
        @JsonProperty("Images")
        public ArrayList<Object> images;
        @SerializedName("Variation3")
        @JsonProperty("Variation3")
        public String variation3;
        @SerializedName("SellerSku")
        @JsonProperty("SellerSku")
        public String sellerSku;
        @SerializedName("ShopSku")
        @JsonProperty("ShopSku")
        public String shopSku;
        public int occupiedStock;
        public int dropshippingStock;
        @SerializedName("Url")
        @JsonProperty("Url")
        public String url;
        public int fulfilmentStock;
        public ArrayList<MultiWarehouseInventory> multiWarehouseInventories;
        public String package_width;
        public String package_height;
        public ArrayList<Object> fblWarehouseInventories;
        public Long special_price;
        public Long price;
        @SerializedName("Variation1")
        @JsonProperty("Variation1")
        public String variation1;
        public ArrayList<Object> channelInventories;
        public String package_length;
        public String package_weight;
        @SerializedName("SkuId")
        @JsonProperty("SkuId")
        public Long skuId;
        public int preorderStock;
        public int withholdingStock;

        public String getDimensionStr() {
            //dài rộng cao (LxWxH)
            return package_length +"x" + package_width  + "x"+package_height;
        }
    }

    public Data data;
    public String code;
    public String request_id;
}
