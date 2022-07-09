package com.wifosell.zeus.taurus.lazada;

import com.google.gson.Gson;
import com.lazada.lazop.api.LazopClient;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.constant.LazadaEcomSyncConst;

public class LazadaClient {
    static LazopClient clientAuth = new LazopClient("https://auth.lazada.com/rest", LazadaEcomSyncConst.APP_ID, LazadaEcomSyncConst.APP_SECRET);
    static LazopClient client = new LazopClient("https://api.lazada.vn/rest", LazadaEcomSyncConst.APP_ID, LazadaEcomSyncConst.APP_SECRET);

    public static LazopClient getClientAuth() {
        return clientAuth;
    }

    public static LazopClient getClient() {
        return client;
    }

    public static <T> T executeMappingModelWithClient(LazopClient lazopClient, LazopRequest request, Class<T> responseBodyType, String token) throws ApiException {
        T contentData = null;
        LazopResponse response = lazopClient.execute(request, token);
        contentData = (T) (new Gson()).fromJson(response.getBody(), responseBodyType);
        return contentData;
    }

    public static <T> T executeMappingModelWithClient(LazopClient lazopClient, LazopRequest request, Class<T> responseBodyType) throws ApiException {
        T contentData = null;
        LazopResponse response = lazopClient.execute(request);
        contentData = (T) (new Gson()).fromJson(response.getBody(), responseBodyType);
        return contentData;
    }

    public static <T> T executeMappingModel(LazopRequest request, Class<T> responseBodyType, String token) throws ApiException {
        T contentData = null;
        LazopResponse response = getClient().execute(request, token);
        contentData = (T) (new Gson()).fromJson(response.getBody(), responseBodyType);
        return contentData;
    }

    public static <T> T executeMappingModel(LazopRequest request, Class<T> responseBodyType) throws ApiException {
        T contentData = null;
        LazopResponse response = getClient().execute(request);
        contentData = (T) (new Gson()).fromJson(response.getBody(), responseBodyType);
        return contentData;
    }
}
