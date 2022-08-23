package com.wifosell.zeus.payload.provider.shopee.hook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ResponseSendoOrderCreateHookPayload extends ResponseBaseHookPayload{

    @SerializedName("data")
    @JsonProperty("data")
    ResponseSendoOrderCreateHookPayloadData Data;

    @Getter
    public static class ResponseSendoOrderCreateHookPayloadData {
        public int id;
        public String order_number;
        public int store_id;
        public String sales_order_merges;

        @SerializedName("is_shop_voucher")
        @JsonProperty("is_shop_voucher")
        public boolean is_shop_voucher;
        @SerializedName("is_merge")
        @JsonProperty("is_merge")
        public boolean is_merge;
        public double mobile_discount_amount;
        @SerializedName("is_self_shipping")
        @JsonProperty("is_self_shipping")
        public boolean is_self_shipping;
        public String bank_name;
        public String bank_code;
        public double installment_bank_fee;
        @SerializedName("is_installment")
        @JsonProperty("is_installment")
        public boolean is_installment;
        public double installment_fee;

        @SerializedName("is_first_order")
        @JsonProperty("is_first_order")
        public Object is_first_order;
        public double sendo_support_fee_to_buyer;
        public Object reason_cancel_code;
        public Object tracking_number;
        public double voucher_value;
        public double seller_shipping_fee;
        public double senpay_free_shipping;
        public double sendo_support_fee;
        public Object affiliate_from_soure;
        public String affiliate_name;
        public double affiliate_total_amount;
        public String carrier_phone;
        public String carrier_code;
        public String carrier_name;
        public String payment_period;
        public double percent_installment;
        public Object buyer_loyalty_amount;
        public double senpay_fee;
        public int tracking_order_source;
        public double cod_fee;
        public double sales_promotion_amount;
        public int types_of_sales_promotion;
        public int sale_product_type;
        public int payment_type;
        public String ship_from_address;
        public int ship_from_ward_id;
        public int ship_from_district_id;
        public int ship_from_region_id;
        public Object ship_from_country_code;
        public String ship_from_zipcode;
        public String ship_to_contact_name;
        public String ship_to_contact_email;
        public String shipping_contact_phone;
        public String ship_to_address;
        public int ship_to_ward_id;
        public int ship_to_district_id;
        public int ship_to_region_id;
        public String ship_to_country_code;
        public String ship_to_zipcode;
        public int buyer_id;
        public String buyer_name;
        public String buyer_phone;
        public String buyer_address;
        public int buyer_district_id;
        public int buyer_region_id;
        public int order_status;
        public int shipping_type;
        public double shipping_fee;
        public double total_amount;
        public double total_amount_buyer;
        public double weight;
        public double actual_weight;
        public int payment_method;
        public String note;
        public Object pay_refund_id;
        public Object pay_cash_transfer_id;
        public Object payment_code;
        public Object refund_trans;
        public Object pay_checkout_id;
        public int payment_status;
        public double sub_total;
        public int ticket_use_date;
        public int payment_status_date_time_stamp;
        public int created_date_time_stamp;
        public int updated_date_time_stamp;
        public int order_date_time_stamp;
        public int order_status_date_time_stamp;
        public boolean can_action;
        public int fds_expired_time;
        public Object payment_type1;
        public Object payment_type1_amount;
        public Object payment_type2;
        public Object payment_type2_amount;
        public double shipping_voucher_amount;
        public Object seller_paylater_fee;
        public String shop_program_name;
        public double shop_program_amount;
        public ArrayList<ResponseSendoOrderSalesOrderDetailPayload> sales_order_details;
        public String type;
        public int partner_id;
    }
}
