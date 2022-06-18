package com.wifosell.zeus.payload.provider.lazada;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@Builder
public class ResponseTokenPayload {
    public String access_token;
    public String refresh_token;
    public String country;

    public int refresh_expires_in;
    public String account_platform;
    public int expires_in;
    public String account;
    public String request_id;
    public ArrayList<CountryUserInfo> country_user_info;
    public class CountryUserInfo{
        public String country;
        public String seller_id;
        public String user_id;
        public String short_code;
    }
}
