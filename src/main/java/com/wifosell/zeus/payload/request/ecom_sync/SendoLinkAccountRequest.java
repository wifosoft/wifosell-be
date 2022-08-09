package com.wifosell.zeus.payload.request.ecom_sync;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SendoLinkAccountRequest {
    public String shopKey;
    public String secretKey;
    public String shopName;
}

