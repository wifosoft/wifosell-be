package com.wifosell.zeus.payload.provider.shopee.hook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSendoOrderSalesOrderDetailPayload{
    public int product_variant_id;
    public String product_name;
    public String store_sku;
    public int quantity;
    public String size_value;
    public String color_value;
    public double price;
    public double sub_total;
    public double weight;
    @SerializedName("is_available")
    @JsonProperty("is_available")
    public boolean is_available;
    public String product_image;
    public String description;
    public int unit_id;
    public String attribute_hash;

    @SerializedName("is_flash_sales")
    @JsonProperty("is_flash_sales")
    public boolean is_flash_sales;
    public double flash_sale_amount;
    @SerializedName("is_combo_discount")
    @JsonProperty("is_combo_discount")
    public boolean is_combo_discount;
    public int combo_parent_product_id;
    public Object flash_deal_type_names;
}
