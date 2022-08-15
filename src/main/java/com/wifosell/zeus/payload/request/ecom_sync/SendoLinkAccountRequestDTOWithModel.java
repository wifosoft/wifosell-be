package com.wifosell.zeus.payload.request.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.wifosell.zeus.model.ecom_sync.EcomAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendoLinkAccountRequestDTOWithModel {
    public String shop_key;
    public String secret_key;

    @Expose(serialize = false, deserialize = false)
    @JsonIgnore
    public EcomAccount ecomAccount;

}
