package com.wifosell.zeus.payload.provider.crawler_competitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public
class BaseCrawlerCompetitorMessage {
    @JsonProperty("package_name")
    @SerializedName("package_name")
    public String packageName;


}
