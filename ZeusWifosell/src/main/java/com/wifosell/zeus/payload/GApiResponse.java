package com.wifosell.zeus.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/*
 *  Response payload for general API with success, message, http status
 */

@Data
@Builder
@JsonPropertyOrder({
        "success",
        "message"
})
public class GApiResponse<T> implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 3128712681565743686L;

    @JsonProperty("success")
    protected Boolean success;

    @JsonProperty("message")
    protected String message;

    @JsonProperty("data")
    protected T data;

    @JsonIgnore
    protected HttpStatus status;

    public GApiResponse() {
    }

    public GApiResponse(boolean success, String message, T data) {
        this.success = success ? Boolean.TRUE : Boolean.FALSE;
        this.message = message;
        this.data = data;
    }

    public GApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public GApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public GApiResponse(Boolean success, String message, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.status = httpStatus;
    }

    public GApiResponse(Boolean success, String message, T data, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.status = httpStatus;
        this.data = data;
    }

    public static <T> GApiResponse<T> success(T data) {
        return new GApiResponse<T>(Boolean.TRUE, "Successfully", data);
    }

    public static <T> GApiResponse<T> fail(T data) {
        return new GApiResponse<T>(Boolean.FALSE, "Failed", data);
    }

    public static <T> GApiResponse<T> success(String message, T data) {
        return new GApiResponse<T>(Boolean.TRUE, message, data);
    }

    public static <T> GApiResponse<T> fail(String message, T data) {
        return new GApiResponse<T>(Boolean.FALSE, message, data);
    }

}
