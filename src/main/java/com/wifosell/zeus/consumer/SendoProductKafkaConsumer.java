package com.wifosell.zeus.consumer;

import com.google.gson.Gson;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.service.SendoProductService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class SendoProductKafkaConsumer {
    @Autowired
    SendoProductService sendoProductService;

    private static final Logger logger = LoggerFactory.getLogger(SendoProductKafkaConsumer.class);

    private KafkaConsumer<String, String> kafkaConsumer;

    public SendoProductKafkaConsumer(String theTechCheckTopicName, Properties consumerProperties) {

        kafkaConsumer = new KafkaConsumer<>(consumerProperties);
        kafkaConsumer.subscribe(Arrays.asList(theTechCheckTopicName));
    }

    /**
     * This function will start a single worker thread per topic.
     * After creating the consumer object, we subscribed to a list of Kafka topics, in the constructor.
     * For this example, the list consists of only one topic. But you can give it a try with multiple topics.
     */
    public void runSingleWorker() {

        while (true) {

            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {

                /*
                Whenever there's a new message in the Kafka topic, we'll get the message in this loop, as
                the record object.
                 */

                /*
                Getting the message as a string from the record object.
                 */
                String message = record.value();
                var responseModel = (new Gson()).fromJson(message, ResponseSendoProductItemPayload.class);
                if (responseModel != null) {
                    sendoProductService.consumeSingleSendoProductFromAPI(responseModel);
                }
                if (responseModel != null) {
                    logger.info("Received sendo product:" + responseModel.name);
                }
                /*
                Logging the received message to the console.
                 */
                logger.info("Received message: " + message);

                {
                    Map<TopicPartition, OffsetAndMetadata> commitMessage = new HashMap<>();

                    commitMessage.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1));

                    kafkaConsumer.commitSync(commitMessage);

                    logger.info("Offset committed to Kafka.");
                }
            }
        }
    }
}
