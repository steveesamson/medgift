package com.apollo.medgift.common;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Message;

public class BaseJob implements Runnable {

    protected Context context;
    protected GiftService giftService;

    public BaseJob(Context context, GiftService giftService){
        this.context = context;
        this.giftService = giftService;
    }
    @Override
    public void run() {
    }


    // Share method for sending notification
    protected void sendNotification(Message message, Class<? extends AppCompatActivity> contentActivityClass){
        NotificationUtil.sendNotification(context, message, contentActivityClass);
    }


}
