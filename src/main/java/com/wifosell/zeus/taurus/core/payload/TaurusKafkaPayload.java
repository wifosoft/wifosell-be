package com.wifosell.zeus.taurus.core.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaurusKafkaPayload<T> {

    @SerializedName(value = "package")
    @JsonProperty("package")
    public String Package;

    @SerializedName(value = "payload_data")
    @JsonProperty("payload_data")
    public T payloadData;

}
