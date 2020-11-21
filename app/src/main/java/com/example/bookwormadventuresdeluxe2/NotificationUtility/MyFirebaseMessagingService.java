package com.example.bookwormadventuresdeluxe2.NotificationUtility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.bookwormadventuresdeluxe2.LoginActivity;
import com.example.bookwormadventuresdeluxe2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static com.example.bookwormadventuresdeluxe2.GlobalApplication.CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onNewToken(@NonNull String token)
    {
        super.onNewToken(token);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
        {
            sendRegistrationToServer(token, currentUser.getUid());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getData().get("Title"), remoteMessage.getData().get("Message"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(String title, String body)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // for lower than api 26
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // removes notification on click
                .build();

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(new Random().nextInt(), notification);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token, String userId)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document("Users/" + userId)
                .update("token", token);
    }
}
