package com.wifosell.zeus.payload.provider.shopee;

import com.wifosell.zeus.payload.provider.lazada.ResponseBasePayload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSendoSellerInfoPayload extends ResponseBasePayload {

    @Getter
    public class Data{
        public String name_company;
        public String logo_url;
        public String name;
        public String verified;
        public String location;
        public String seller_id;

        public String email;
        public String short_code;
        public String cb;
        public String status;
        public String shop_key;
        public String secret_key;

    }
    public Data data;

}
