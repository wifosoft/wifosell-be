package com.wifosell.zeus.payload.provider.shopee.hook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBaseHookPayload {
    public String type;
    public int store_id;
    public String shop_key;
}
