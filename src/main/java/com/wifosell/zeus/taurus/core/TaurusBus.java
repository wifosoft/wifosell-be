package com.wifosell.zeus.taurus.core;

import com.google.gson.Gson;
import com.wifosell.zeus.taurus.core.payload.TaurusKafkaPayload;

public class TaurusBus {
    public static <T> TaurusKafkaPayload buildPayloadMessage(T data , String pkgName){
        TaurusKafkaPayload payloadMessage = new TaurusKafkaPayload<T>();
        payloadMessage.setPackage(pkgName);
        payloadMessage.setPayloadData( data );
        return payloadMessage;
    }
    public static <T> String buildPayloadMessageString(T data , String pkgName){
        TaurusKafkaPayload payloadMessage = new TaurusKafkaPayload<T>();
        payloadMessage.setPackage(pkgName);
        payloadMessage.setPayloadData( data );
        return (new Gson()).toJson(payloadMessage);
    }
}
