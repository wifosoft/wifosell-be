package com.wifosell.zeus.taurus.sendo;

import com.google.gson.Gson;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTO;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SendoServiceClient {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client;

    String baseUrl = "http://localhost:3001";

    public SendoServiceClient(String baseUrl) {
        client = new OkHttpClient();
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public <T> T Get(String path, Class<T> responseBodyType) {
        Request request = new Request.Builder()
                .url(getBaseUrl() + path)
                .get()
                .build();

        T contentData = null;
        okhttp3.Response response;
        try {
            response = client.newCall(request).execute();
            if (response.body() == null) {
                return null;
            }
            String responseBody = response.body().string();
            contentData = (T) (new Gson()).fromJson(responseBody, responseBodyType);
            return contentData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public <K, T> T Post(String path, Map<String, String> headers,K requestData, Class<T> responseBodyType) {
        String bodyJson = (new Gson()).toJson(requestData);
        Headers headerbuild = Headers.of(headers);

        RequestBody requestBody = RequestBody.create(JSON, bodyJson); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url(getBaseUrl() + path)
                .headers(headerbuild)
                .post(requestBody)
                .build();

        T contentData = null;
        okhttp3.Response response;
        try {
            response = client.newCall(request).execute();
            if (response.body() == null) {
                return null;
            }

            String responseBody = response.body().string();
            contentData = (T) (new Gson()).fromJson(responseBody, responseBodyType);
            return contentData;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }



    public <K, T> T Post(String path, SendoLinkAccountRequestDTO sendoLinkAccountRequestDTO, K requestData, Class<T> responseBodyType) {
        String bodyJson = (new Gson()).toJson(requestData);
        HashMap<String, String> headerAuth = new HashMap<String, String>();
        headerAuth.put("shop_key", sendoLinkAccountRequestDTO.getShop_key());
        headerAuth.put("secret_key", sendoLinkAccountRequestDTO.getSecret_key());
        Headers headerbuild = Headers.of(headerAuth);

        RequestBody requestBody = RequestBody.create(JSON, bodyJson); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url(getBaseUrl() + path)
                .headers(headerbuild)
                .post(requestBody)
                .build();

        T contentData = null;
        okhttp3.Response response;
        try {
            response = client.newCall(request).execute();
            if (response.body() == null) {
                return null;
            }

            String responseBody = response.body().string();
            contentData = (T) (new Gson()).fromJson(responseBody, responseBodyType);
            return contentData;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public <K, T> T Post(String path, K requestData, Class<T> responseBodyType) {
        String bodyJson = (new Gson()).toJson(requestData);

        RequestBody requestBody = RequestBody.create(JSON, bodyJson); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url(getBaseUrl() + path)
                .post(requestBody)
                .build();

        T contentData = null;
        okhttp3.Response response;
        try {
            response = client.newCall(request).execute();
            if (response.body() == null) {
                return null;
            }

            String responseBody = response.body().string();
            contentData = (T) (new Gson()).fromJson(responseBody, responseBodyType);
            return contentData;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


}
