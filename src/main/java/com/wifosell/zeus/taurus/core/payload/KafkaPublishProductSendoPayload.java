package com.wifosell.zeus.taurus.core.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.wifosell.zeus.payload.request.ecom_sync.SendoCreateOrUpdateProductPayload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaPublishProductSendoPayload {
    public String secret_key;
    public String shop_key;

    @JsonProperty("publish_data_json")
    @SerializedName(value = "publish_data_json")
    public SendoCreateOrUpdateProductPayload publish_data_json;

}
