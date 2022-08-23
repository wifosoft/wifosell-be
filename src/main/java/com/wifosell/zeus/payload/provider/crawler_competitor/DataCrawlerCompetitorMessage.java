package com.wifosell.zeus.payload.provider.crawler_competitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public
class DataCrawlerCompetitorMessage extends BaseCrawlerCompetitorMessage {

    @JsonProperty("data")
    @SerializedName("data")
    DataCrawlerCompetitorMessageData data;

    @Getter
    @Setter
    public static class DataCrawlerCompetitorMessageData {
        private Long id;
        private String url;
        private Long price;
    }

}
