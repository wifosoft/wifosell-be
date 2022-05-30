package com.wifosell.zeus.taurus.lazada;

import com.google.gson.Gson;
import com.lazada.lazop.api.LazopClient;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;
import com.wifosell.zeus.constant.LazadaEcomSyncConst;
import com.wifosell.zeus.payload.provider.lazada.ResponseTokenPayload;

public class LazadaClient {
    static LazopClient client = new LazopClient("https://auth.lazada.com/rest", LazadaEcomSyncConst.APP_ID, LazadaEcomSyncConst.APP_SECRET);

    public static LazopClient getClient() {
        return client;
    }
    public static <T> T executeMappingModel(LazopRequest request,  Class<T> responseBodyType) throws ApiException {
        T contentData = null;
        LazopResponse response = getClient().execute(request);
        contentData = (T) (new Gson()).fromJson(response.getBody(), responseBodyType);
        return contentData;
    }
}
