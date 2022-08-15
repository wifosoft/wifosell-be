package com.wifosell.lazada.modules.image.payload;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class LazadaMigrateImagesBatchResponse {
    public static LazadaMigrateImagesBatchResponse fromJson(String json) {
        return new Gson().fromJson(json, LazadaMigrateImagesBatchResponse.class);
    }

    @SerializedName("batch_id")
    private String batchId;
}
