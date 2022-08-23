package com.wifosell.zeus.consumer;

import com.google.gson.Gson;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseBaseHookPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderCreateHookPayload;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderUpdateHookPayload;
import com.wifosell.zeus.service.SendoOrderService;
import com.wifosell.zeus.service.SendoProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class SendoProductConsumer {
    @Autowired
    SendoOrderService sendoOrderService;

    @Autowired
    SendoProductService sendoProductService;
    Logger LOG = LoggerFactory.getLogger(SendoProductConsumer.class);


    @KafkaListener(topics = "sendo_product", containerFactory = "kafkaListenerContainerFactory", groupId = "${spring.kafka.groupId}",  properties = "value.deserializer:org.apache.kafka.common.serialization.StringDeserializer")
    void listener(String data) {
        LOG.info("[+] Kafka receive message Length : {}" , data.length());

        var responseModel =  (new Gson()).fromJson(data, ResponseSendoProductItemPayload.class);
        if(responseModel !=null){
            sendoProductService.consumeSingleSendoProductFromAPI(responseModel);
        }
    }


    @KafkaListener(topics = "sendo_order", containerFactory = "kafkaListenerContainerFactory", groupId = "${spring.kafka.groupId}",  properties = "value.deserializer:org.apache.kafka.common.serialization.StringDeserializer")
    void orderListener(String data) {
        LOG.info("[+] Kafka receive message sendo_order Length : {}" , data.length());

        try {
            var responseModel = (new Gson()).fromJson(data, ResponseBaseHookPayload.class);
            if (responseModel != null) {
                switch (responseModel.type) {
                    case "SALESORDER.CREATE":
                        var responseOrderCreateModel = (new Gson()).fromJson(data, ResponseSendoOrderCreateHookPayload.class);
                        sendoOrderService.consumeOrderService(responseModel.getShop_key(), responseOrderCreateModel);
                        break;
                    case "SALESORDER.UPDATE":
                        var responseOrderUpdateModel = (new Gson()).fromJson(data, ResponseSendoOrderUpdateHookPayload.class);
                        break;
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
            LOG.error("Kafka orderListener");
        }
    }

}