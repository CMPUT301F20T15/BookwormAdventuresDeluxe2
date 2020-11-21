package com.example.bookwormadventuresdeluxe2.NotificationUtility;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService
{
    @Headers(
            {
                    "Authorization:key=AAAAxQez4bg:APA91bHXkCnrOfTnVcmkfxC-9hqMrvvNJdHb_qEcG4-j0YxbUaHFq9kB6pzHkU4XJSJhyTt7lpXgCyXyO8jJRokJIKErI9JZVGkCRV1nIBUb1f1intxwHai1RCTrhObXX6nQ8rK4AHDN",
                    "Content-Type:application/json"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification (@Body NotificationData body);
}
