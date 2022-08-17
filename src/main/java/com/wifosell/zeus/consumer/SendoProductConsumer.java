package com.wifosell.zeus.consumer;


import com.google.gson.Gson;
import com.wifosell.zeus.config.kafka.KafkaConsumerConfig;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.service.SendoProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class SendoProductConsumer {


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

}