package com.wifosell.zeus.payload.request.ecom_sync;

import com.wifosell.zeus.payload.provider.lazada.ResponseTokenPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

@Builder
public class EcomAccountLazadaCallbackPayloadRequest {
    public Long userId;
    public String signature;
    public String code;
    public String feCallback;
    public ResponseTokenPayload tokenAuthResponse;
}
