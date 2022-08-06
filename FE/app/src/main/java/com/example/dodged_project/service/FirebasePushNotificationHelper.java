package com.example.dodged_project.service;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dodged_project.MainActivity;
import com.example.dodged_project.R;

public class FirebasePushNotificationHelper {
    public static void displayPushNotification(Context context, String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.FIREBASE_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }
}
