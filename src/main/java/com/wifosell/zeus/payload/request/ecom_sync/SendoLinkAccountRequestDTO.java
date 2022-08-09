package com.wifosell.zeus.payload.request.ecom_sync;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendoLinkAccountRequestDTO {
    public String shop_key;
    public String secret_key;
    public String ecom_id;
}
