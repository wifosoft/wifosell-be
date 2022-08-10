package com.wifosell.zeus.payload.provider.ecom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountInfoPayloadData {
    public String name;
    public String verified;
    public String location;
    public String seller_id;
    public String email;
    public String secret_key;
    public String shop_key;
    public String short_code;
    public String cb;
    public String status;
}
