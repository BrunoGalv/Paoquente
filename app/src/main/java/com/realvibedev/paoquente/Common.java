package com.realvibedev.paoquente;

import com.realvibedev.paoquente.Remote.APIService;
import com.realvibedev.paoquente.Remote.RetrofitClient;

/**
 * Created by bruno on 29/03/2018.
 */

public class Common {

    public static String currentToken = "";

    private static String baseUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient(){
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }
}
