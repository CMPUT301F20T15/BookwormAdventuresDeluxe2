package com.example.bookwormadventuresdeluxe2.NotificationUtility;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookwormadventuresdeluxe2.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationHandler
{
    public static final String TAG = "NotificationHandler";

    public static void sendNotification(String message, String userId)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
                .collection("Users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {

                        }
                    }
                });


        String userToken = "fgwApOh-TKiSUVp5PslbnT:APA91bEGRvCxbZFiTLyov3SiIq7C1NmRUa3LfP_vg-d5GkqVmHBh3iC8KyAaChg8Q_5qSEilBymfJHM62KAbXCfF8xogheXvs7-5RbCL6sYvUuv1t6jV1iUTX8F0eNjtgA5dgJAmr9fh";
        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
////        get user token from firebase
        String title = "Title";
        message = "Body";
        Data data = new Data(title, message);
        NotificationData notificationData = new NotificationData(data, userToken);
        apiService.sendNotification(notificationData).enqueue(new Callback<MyResponse>()
        {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
            {
                if (response.code() == 200)
                {
                    if (response.body().success != 1)
                    {
                        Log.d("Login", "Notification send failed");
                    }
                }

                Log.d("Login", String.valueOf(response.code()) + " - " + String.valueOf(response.body().success));
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t)
            {
                Log.d("Login", "on failure");
            }
        });
    }

    /**
     * Stores app token in user document in firebase
     *
     * @param userId
     */
    public static void addAppToken(String userId)
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
                        String token = task.getResult();

                        // Store token
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(userId).update("token", token);
                    }
                });
    }
}
