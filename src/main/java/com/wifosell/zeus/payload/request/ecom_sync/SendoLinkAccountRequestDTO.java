package com.wifosell.zeus.payload.request.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendoLinkAccountRequestDTO {
    public String shop_key;
    public String secret_key;

    @JsonIgnore
    public EcomAccount ecomAccount;


}
