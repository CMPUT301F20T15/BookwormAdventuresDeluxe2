package com.example.bookwormadventuresdeluxe2.NotificationUtility;

/**
 * Used to send push notifications to users and update FCM registration token for users
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookwormadventuresdeluxe2.FirebaseUserGetSet;
import com.example.bookwormadventuresdeluxe2.GlobalApplication;
import com.example.bookwormadventuresdeluxe2.LoginActivity;
import com.example.bookwormadventuresdeluxe2.Notification;
import com.example.bookwormadventuresdeluxe2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationHandler
{
    public static final String TAG = "NotificationHandler";
    private static Context context = GlobalApplication.getAppContext();


    /**
     * Get User FCM token, create raw notification with data passed in
     *
     * @param title    The title of the notification
     * @param message  The title of the message
     * @param username The username of the receiver
     */
    public static void sendNotification(String title, String message, String username, HashMap<String, String> inAppNotification)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /* Find user FCM token and process notification */
        FirebaseUserGetSet.getUser(username, (user ->
        {
            /* Send push notification */
            String token = user.getFCMtoken();
            if (token != null)
            {
                Data data = new Data(title, message);
                NotificationData notificationData = new NotificationData(data, token);
                processNotification(notificationData);
            }

            /* Add to user notification collection */
            FirebaseFirestore.getInstance().collection(context.getString(R.string.users_collection))
                    .document(user.getDocumentId())
                    .collection(context.getString(R.string.notifications_collection))
                    .add(inAppNotification);
            /* Increment user notification count */
            FirebaseUserGetSet.incrementNotificationCount(user.getDocumentId());
        }));
    }


    /**
     * Carry outs the process of sending a notification
     *
     * @param notificationData Filled in notification with data and token
     */
    private static void processNotification(NotificationData notificationData)
    {
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        apiService.sendNotification(notificationData).enqueue(new Callback<MyResponse>()
        {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
            {
                if (response.code() == 200)
                {
                    if (response.body().success != 1)
                    {
                        Log.d(TAG, "Notification send failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t)
            {
                Log.d(TAG, "apiService send nottifcation on failure");
            }
        });
    }

    /**
     * Checks if current users FCM token is valid and updates accordingly
     *
     * @param oldToken Old FCM token of user which is checked
     * @param userId   The user ID of the user to register FCM token
     */
    public static void updateFCMToken(String oldToken, String userId)
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>()
                {
                    @Override
                    public void onComplete(@NonNull Task<String> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String FCMtoken = task.getResult();

                        // Update FCMtoken if current token invalid or null
                        if (oldToken != FCMtoken)
                        {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection(context.getString(R.string.users_collection)).document(userId).update(context.getString(R.string.firestore_user_token_field), FCMtoken);
                        }
                    }
                });
    }
}
