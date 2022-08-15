package com.wifosell.lazada.modules.image.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class LazadaMigrateImagesResponse {
    public static LazadaMigrateImagesResponse fromJson(String json) {
        return new Gson().fromJson(json, LazadaMigrateImagesResponse.class);
    }

    @SerializedName("data")
    private Data data;

    @Getter
    public static class Data {
        @SerializedName("images")
        List<Image> images;

        @SerializedName("errors")
        List<Error> errors;

        @Getter
        public static class Image {
            @SerializedName("hash_code")
            private String hashCode;

            @SerializedName("url")
            private String url;
        }

        @Getter
        public static class Error {
            @SerializedName("msg")
            private String msg;

            @SerializedName("field")
            private String field;

            @SerializedName("original_url")
            private String originalUrl;
        }
    }
}
