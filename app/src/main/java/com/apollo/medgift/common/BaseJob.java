package com.apollo.medgift.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apollo.medgift.R;

public class BaseJob extends Worker {

    private static int notificationId = 1;

    public BaseJob(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }

    // Share method for sending notification
    protected void sendNotification(String notificationTitle, String notificationText, Class<? extends AppCompatActivity> contentActivityClass){
        NotificationUtil.sendNotification(getApplicationContext(), notificationTitle, notificationText, contentActivityClass);
    }
}
