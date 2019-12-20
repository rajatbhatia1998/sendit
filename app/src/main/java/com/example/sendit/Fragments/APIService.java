package com.example.sendit.Fragments;

import com.example.sendit.Notification.MyResponse;
import com.example.sendit.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAMkRtUGM:APA91bEET2kJALGERMSwqyDbjZFFi66nA4HUlLIbJ0MBpRH1fxL1Zs80B9B1UxiqAxy6_JpwIaDaATwctvDsnL6HHwqQPW9X-xBNl51hmxAIDYdZOzK-PszgPCawni8MT0Kt8YWoUr0d"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
