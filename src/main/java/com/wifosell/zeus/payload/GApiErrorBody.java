package com.wifosell.zeus.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonPropertyOrder({
        "error_code",
        "error_message",
        "error_data"
})
@AllArgsConstructor
public class GApiErrorBody<T> {
    @JsonProperty("error_code")
    private int errorCode;
    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("error_data")
    T data;

    public static <T> GApiErrorBody<T> makeErrorBody(EAppExceptionCode code, String message, T data) {
        return new GApiErrorBody<T>(code.getValue(), message
                , data);
    }

    public static <T> GApiErrorBody<T> makeErrorBody(EAppExceptionCode code) {
        return new GApiErrorBody<T>(code.getValue(), code.name()
                , null);
    }

    public static <T> GApiErrorBody<T> makeErrorBody(EAppExceptionCode code, String message) {
        return new GApiErrorBody<T>(code.getValue(), message, null);
    }


}
