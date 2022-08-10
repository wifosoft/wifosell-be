package com.wifosell.zeus.payload.provider.shopee;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
public class ResponseSendoProductItemPayload {
    public String _id;
    public Date created_at;
    public Date updated_at;
    public String shop_relation_id;
    public Long id;
    public String name;
    public String sku;
    public Long price;
    public Long weight;
    public boolean stock_availability;
    public Long stock_quantity;
    public String description;
    public Long cat_4_id;
    public Long status;
    public Object tags;
    public Long updated_date_timestamp;
    public Long created_date_timestamp;
    public String seo;
    public String link;
    public ArrayList<Object> relateds;
    public Object seo_keyword;
    public Object seo_title;
    public Object seo_description;
    public Long seo_score;
    public String image;
    public String category_4_name;
    public Long promotion_price;
    public Long brand_id;
    public Object brand_name;
    public String updated_user;
    public String url_path;
    public Object video_links;
    public Long height;
    public Long length;
    public Long width;
    public Long unit_id;
    public Avatar avatar;
    public ArrayList<Picture> pictures;
    public ArrayList<Object> certificate_file;
    public ArrayList<Attribute> attributes;
    public Long special_price;
    public Object promotion_from_date_timestamp;
    public Object promotion_to_date_timestamp;
    public boolean is_promotion;
    public ExtendedShippingPackage extended_shipping_package;
    public ArrayList<Variant> variants;
    public boolean is_config_variant;
    public boolean is_invalid_variant;
    public Voucher voucher;
    public ArrayList<Long> product_category_types;
    public Object is_flash_sales;
    public Object campaign_status;
    public boolean can_edit;
    public ArrayList<Object> sendo_video;
    public ArrayList<Object> installments;

    @Getter
    @Setter
    public class Attribute {
        public Long attribute_id;
        public Long attribute_type;
        public String attribute_name;
        public boolean attribute_is_required;
        public String attribute_code;
        public boolean attribute_is_custom;
        public boolean attribute_is_checkout;
        public boolean attribute_is_image;
        public ArrayList<AttributeValue> attribute_values;
    }

    @Getter
    @Setter
    public class AttributeValue {
        public Long id;
        public String value;
        public Object attribute_img;
        public boolean is_selected;
        public boolean is_custom;
    }

    @Getter
    @Setter
    public class Avatar {
        public String picture_url;
    }

    @Getter
    @Setter
    public class ExtendedShippingPackage {
        public boolean is_using_instant;
        public boolean is_using_in_day;
        public boolean is_self_shipping;
        public boolean is_using_standard;
        public boolean is_using_eco;
    }

    @Getter
    @Setter
    public class Picture {
        public String picture_url;
    }


    @Getter
    @Setter
    public class Variant {
        public ArrayList<VariantAttribute> variant_attributes;
        public Long variant_is_promotion;
        public String variant_sku;
        public Long variant_price;
        public Long variant_special_price;
        public Long variant_quantity;
        public Object variant_promotion_start_date_timestamp;
        public Object variant_promotion_end_date_timestamp;
        public Object variant_is_flash_sales;
        public Object variant_campaign_status;
        public String variant_attribute_hash;
        public Object message;
    }

    @Getter
    @Setter
    public class VariantAttribute {
        public Long attribute_id;
        public String attribute_code;
        public Long option_id;
    }

    @Getter
    @Setter
    public class Voucher {
        public Long product_type;
        public Object start_date;
        public Object end_date;
        public Object is_check_date;
    }
}
