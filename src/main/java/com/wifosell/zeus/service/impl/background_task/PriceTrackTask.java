package com.wifosell.zeus.service.impl.background_task;

import com.google.gson.Gson;
import com.wifosell.zeus.payload.provider.crawler_competitor.DataCrawlerCompetitorMessage;
import com.wifosell.zeus.service.PriceTrackService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PriceTrackTask {
    private static final Logger logger = LoggerFactory.getLogger(PriceTrackTask.class);
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    public PriceTrackService priceTrackService;

    @Recurring(id = "price-track-task", cron = "*/18 * * * *")
    @Job(name = "Price track task service")
    public void priceTaskListService() {
        var gson = new Gson();
        Map<Long, String> listPriceTrackMap = priceTrackService.getMapActiveCompetitorUrls();
        try {
            if (listPriceTrackMap == null) {
                logger.error("priceTaskListService error null map");
            }
            for (Map.Entry<Long, String> item : listPriceTrackMap.entrySet()) {
                try {
                    String payloadStr = "";


                    //build data paylad
                    var dataCrawlerPayload = new DataCrawlerCompetitorMessage.DataCrawlerCompetitorMessageData();
                    dataCrawlerPayload.setId(item.getKey());
                    dataCrawlerPayload.setUrl(item.getValue());

                    //build message
                    DataCrawlerCompetitorMessage dataCrawlerCompetitorMessage = new DataCrawlerCompetitorMessage();
                    dataCrawlerCompetitorMessage.setPackageName("wifosell.crawler.competitor");
                    dataCrawlerCompetitorMessage.setData(dataCrawlerPayload);

                    //convert to string
                    payloadStr = gson.toJson(dataCrawlerCompetitorMessage);
                    kafkaTemplate.send("crawler_competitor", payloadStr);
                    logger.info("[+] priceTaskListService single message task crawler {}", payloadStr);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    logger.info("[+] priceTaskListService single message exception {}", item.getValue());

                }
            }
            logger.info("[+] priceTaskListService processed map {}", listPriceTrackMap.size());

            //Page<ProductResponse> responses = products.map(product -> new ProductResponse(product, warehouseIds));

        } catch (Exception exception) {
            exception.printStackTrace();
            logger.error("fetchSendoProductRecurring error ");
        }
    }
}
