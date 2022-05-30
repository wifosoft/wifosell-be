package com.wifosell.zeus.constant;

public class LazadaEcomSyncConst {
    public static String APP_ID = "117995";
    public static String APP_SECRET =  "Ww7amuoeFAbgSjObP2R5Ib4IU88eBIhK";
    public static String FE_RETURN_URL = "https://wifosell-dev.com:8888";
    public static String FORMAT_CALLBACK = "https://auth.lazada.com/oauth/authorize?response_type=code&force_auth=true&redirect_uri=https://wifosell-dev.com:8888/api/ecom_sync/ecom_account/callback_lazada?data=%s&client_id=" + APP_ID;
}
