package com.wifosell.zeus.payload.provider.shopee;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class ResponseSendoCategoryPayload {
    @SerializedName("id")
    Long id;
    @SerializedName("name")
    String name;
    @SerializedName("parent_id")
    Long parentId;
    @SerializedName("level")
    Integer level;
    @SerializedName("is_config_variant")
    boolean isConfigVariant;

    public boolean isLeaf() {
        return level == 4;
    }

    public boolean isRoot() {
        return level == 2;
    }
}
