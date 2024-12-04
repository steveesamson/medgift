package com.apollo.medgift.common;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.apollo.medgift.R;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.models.Notification;

public class NotificationUtil {
    private static int NOTIFICATION_ID = 1;

    public static  final String CHANNEL_ID = "MEDGIFT_CHANNEL";
    // Init notification
    public static void createNotificationChannel(AppCompatActivity activity,  String channelName, String channelDescription) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(channelDescription);
        NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        // Do this upfront
        requestNotificationPermissions(activity);
    }

    private static void requestNotificationPermissions(AppCompatActivity activity) {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( activity, new String[] { android.Manifest.permission.POST_NOTIFICATIONS }, 1);
        }
    }

    // Share method for sending notification
    public static  void sendNotification(Context context, Message message, Class<? extends AppCompatActivity> contentActivityClass){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle(message.getTitle());
        builder.setContentText(message.getBody());
        builder.setSmallIcon(R.drawable.ic_notification);
        if(contentActivityClass != null){
            Intent intent = new Intent(context, contentActivityClass);
            intent.putExtra(Message.STORE, message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[] { android.Manifest.permission.POST_NOTIFICATIONS }, NOTIFICATION_ID + 1);
            return;
        }
        managerCompat.notify(NOTIFICATION_ID++, builder.build());
    }

    public static void saveNotification(Message message, String createdFor){
        Notification notification = new Notification();
        notification.setBody(message.getBody());
        notification.setTitle(message.getTitle());
        notification.setModelKey(message.getPayLoad().getKey());
        notification.setModelName(message.getNotificationType().name());
        notification.setCreatedFor(createdFor);
        Firebase.save(notification, Notification.STORE, (task, key) ->{ });
    }
}
