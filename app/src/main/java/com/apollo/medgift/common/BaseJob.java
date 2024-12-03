package com.apollo.medgift.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.apollo.medgift.models.Message;

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
    protected void sendNotification(Message message, Class<? extends AppCompatActivity> contentActivityClass){
        NotificationUtil.sendNotification(getApplicationContext(), message, contentActivityClass);
    }
}
