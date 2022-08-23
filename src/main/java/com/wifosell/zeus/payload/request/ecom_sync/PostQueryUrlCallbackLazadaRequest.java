package com.wifosell.zeus.payload.request.ecom_sync;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostQueryUrlCallbackLazadaRequest {
    public String feCallbackDomain;
    public String feCallbackUrl;
}
