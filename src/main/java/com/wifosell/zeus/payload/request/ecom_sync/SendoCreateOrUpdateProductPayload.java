package com.wifosell.zeus.payload.request.ecom_sync;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SendoCreateOrUpdateProductPayload {
    public int id;
    public String name;
    public String sku;
    public int price;
    public int weight;
    public boolean stock_availability;
    public String description;
    public int cat_4_id;
    public String tags;
    public ArrayList<Related> relateds;
    public String seo_keyword;
    public String seo_title;
    public String seo_description;
    public String product_image;
    public int brand_id;
    public String video_links;
    public int height;
    public int length;
    public int width;
    public int unit_id;
    public int stock_quantity;
    public Avatar avatar;
    public ArrayList<Picture> pictures;
    public ArrayList<CertificateFile> certificate_file;
    public ArrayList<Attribute> attributes;
    public Date promotion_from_date;
    public Date promotion_to_date;

    @SerializedName("is_promotion")
    @JsonProperty("is_promotion")
    public boolean is_promotion;
    public ExtendedShippingPackage extended_shipping_package;
    public ArrayList<Variant> variants;


    @SerializedName("is_config_variant")
    @JsonProperty("is_config_variant")
    public boolean is_config_variant;
    public int special_price;
    public Voucher voucher;

    @Getter
    @Setter
    public static class Attribute {
        public int attribute_id;
        public String attribute_name;
        public String attribute_code;
        public boolean attribute_is_custom;
        public boolean attribute_is_required;

        public boolean attribute_is_checkout;
        public ArrayList<AttributeValue> attribute_values;
    }

    @Getter
    @Setter
    public static class AttributeValue {
        public int id;
        public String value;
        public String attribute_img;

        @SerializedName("is_selected")
        @JsonProperty("is_selected")
        public boolean is_selected;

        @SerializedName("is_custom")
        @JsonProperty("is_custom")
        public boolean is_custom;
    }

    @Getter
    @Setter
    public static class Avatar {
        public String picture_url;
    }

    @Getter
    @Setter
    public class CertificateFile {
        public int id;
        public String table_name;
        public int table_id;
        public String file_name;
        public String attachment_url;
        public int display_order;
    }

    @Getter
    @Setter
    public static class ExtendedShippingPackage {

        @SerializedName("is_using_instant")
        @JsonProperty("is_using_instant")
        public boolean is_using_instant;

        @SerializedName("is_using_in_day")
        @JsonProperty("is_using_in_day")
        public boolean is_using_in_day;

        @SerializedName("is_self_shipping")
        @JsonProperty("is_self_shipping")
        public boolean is_self_shipping;

        @SerializedName("is_using_standard")
        @JsonProperty("is_using_standard")
        public boolean is_using_standard;


        @SerializedName("is_using_eco")
        @JsonProperty("is_using_eco")
        public boolean is_using_eco;
    }

    @Getter
    @Setter
    public static class Picture {
        public String picture_url;
    }

    @Getter
    @Setter
    public static class Related {
        public int id;
        public String name;
        public String sku;
        public String category_name;
        public int price;
        public int status;
        public String image;
    }

    @Getter
    @Setter
    public static class Variant {
        public ArrayList<VariantAttribute> variant_attributes;
        public String variant_sku;
        public int variant_price;
        public Date variant_promotion_start_date;
        public Date variant_promotion_end_date;
        public int variant_special_price;
        public int variant_quantity;
    }

    @Getter
    @Setter
    public static class VariantAttribute {
        public int attribute_id;
        public String attribute_code;
        public int option_id;
    }

    @Getter
    @Setter
    public static class Voucher {
        public int product_type;
        public Date start_date;
        public Date end_date;

        @SerializedName("is_check_date")
        @JsonProperty("is_check_date")
        public boolean is_check_date;
    }

}
