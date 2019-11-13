package com.realvibedev.paoquente.Remote;

import com.realvibedev.paoquente.model.MyResponse;
import com.realvibedev.paoquente.model.Sender;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by bruno on 29/03/2018.
 */

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAiTNQS2g:APA91bEnKNR9-CD8aanJpThXAxyCx9xwcF20jfTj8U-s_NrVL2Mn9tvtNnabrwnRZO3NQ_21YWQs2fYKdzyZBrTCArj1RwD60vlfqmDee3uH4FdHbtn_IkKf1c_xnhcjh9ZVvPUMmBNl"
    })
    @POST("fcm/send")
    retrofit2.Call<MyResponse> sendNotification(@Body Sender sender);
}
