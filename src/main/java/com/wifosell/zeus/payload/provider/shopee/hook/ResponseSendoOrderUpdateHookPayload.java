package com.wifosell.zeus.payload.provider.shopee.hook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSendoOrderUpdateHookPayload extends ResponseBaseHookPayload {

    @SerializedName("data")
    @JsonProperty("data")
    ResponseSendoOrderUpdateHookPayload.ResponseSendoOrderUpdateHookPayloadData Data;

    @Getter
    public static class ResponseSendoOrderUpdateHookPayloadData {
        @SerializedName("data")
        @JsonProperty("data")
        public ResponseSendoOrderUpdateHookPayloadDataData data;
        public String order_number;
        public String order_action;
        public String cancel_by;
        public int order_status;
        public String reason_cancel_code;
        public String cancel_reason;
        public int cancel_date_timestamp;

        @Getter
        public static class ResponseSendoOrderUpdateHookPayloadDataData {
            public String cancel_by;
            public int order_status;
            public String reason_cancel_code;
            public String cancel_reason;
            public int cancel_date_timestamp;
        }
    }

}
