package com.example.mylibrarymanagementapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class LibraryBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LibraryBroadcastReceiver", "onReceive triggered: " + intent.getAction());

        // Initialisation de la base de données
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);

        if (intent.getAction() != null) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                // Retrieve the latest book details
                String latestBookDetails = dbHelper.getLatestBook();
                sendNotification(context, "Latest Book", latestBookDetails);
            }

            if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
                long latestTimestamp = dbHelper.getLatestDateTimestamp();
                long currentTimestamp = System.currentTimeMillis();

                if (latestTimestamp == 0) {
                    sendNotification(context, "No books found", "add books to see the effect of changing the date");
                } else if (currentTimestamp < latestTimestamp) {
                    sendNotification(context, "Attention", "The system date is earlier than the last added date.");
                } else {
                    sendNotification(context, "Date modified", "the date is modified successfully.");
                }
            }
        }
    }

    private void sendNotification(Context context, String title, String message) {
        String channelId = "library_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Library Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setAutoCancel(true);

        notificationManager.notify(1, notificationBuilder.build());
    }
}
