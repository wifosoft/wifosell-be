package com.wifosell.zeus.consumer;

import com.google.gson.Gson;
import com.oracle.tools.packager.Log;
import com.wifosell.zeus.payload.provider.crawler_competitor.BaseCrawlerCompetitorMessage;
import com.wifosell.zeus.payload.provider.crawler_competitor.DataCrawlerCompetitorMessage;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseBaseHookPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderCreateHookPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderUpdateHookPayload;
import com.wifosell.zeus.service.PriceTrackService;
import com.wifosell.zeus.service.SendoOrderService;
import com.wifosell.zeus.service.SendoProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
class CrawlerCompetitorConsumer {
    @Autowired
    SendoOrderService sendoOrderService;

    @Autowired
    SendoProductService sendoProductService;

    @Autowired
    PriceTrackService priceTrackService;
    Logger LOG = LoggerFactory.getLogger(CrawlerCompetitorConsumer.class);


    @KafkaListener(topics = "crawler_competitor_result", containerFactory = "kafkaListenerContainerFactory", groupId = "${spring.kafka.groupId}", properties = "value.deserializer:org.apache.kafka.common.serialization.StringDeserializer")
    void orderListener(String data) {
        LOG.info("[+] Kafka receive message crawler_competitor Length : {}", data.length());

        try {
            var responseModel = (new Gson()).fromJson(data, BaseCrawlerCompetitorMessage.class);
            if (responseModel != null) {
                switch (responseModel.getPackageName()) {
                    case "wifosell.crawler.competitor.result":
                        var dataResultCrawler = (new Gson()).fromJson(data, DataCrawlerCompetitorMessage.DataCrawlerCompetitorMessageData.class);
                        if (dataResultCrawler.getId() == null) {
                            LOG.info("[-] ID is null...");
                            return;
                        }
                        try {
                            int statusTriggerTrack = priceTrackService.trigger(dataResultCrawler.getId(), new BigDecimal(dataResultCrawler.getPrice()));
                            LOG.info("[-] Check triggered: {} - Price: {} - Status: {}", dataResultCrawler.getId(), dataResultCrawler.getPrice(), statusTriggerTrack);
                        } catch (Exception exception) {
                            LOG.info("[-] Check triggered {} - Price {}", dataResultCrawler.getId(), dataResultCrawler.getPrice());
                            exception.printStackTrace();
                        }
                        break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            LOG.error("Kafka crawler exception");
        }
    }

}