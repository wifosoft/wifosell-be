package com.wifosell.zeus.payload.provider.shopee;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResponseLinkAccountPayload  extends SendoServiceResponseBase{

    ResponseLinkAccountPayloadData data;

    @Getter
    @Setter
    public class ResponseLinkAccountPayloadData {
        public String _id;
        public Date created_at;
        public Date updated_at;
        //public String ecom_id;
        public String shop_key;
        public String secret_key;
        public String token;
        public Date expires;
    }
}
