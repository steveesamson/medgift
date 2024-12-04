package com.apollo.medgift.common;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Message;

public class BaseJob implements Runnable {

    protected Context context;
    protected GiftService giftService;
    protected String createdFor;
    public BaseJob(Context context, GiftService giftService, String createdFor){
        this.context = context;
        this.giftService = giftService;
        this.createdFor = createdFor;
    }
    @Override
    public void run() {
    }


    // Share method for sending notification
    protected void sendNotification(Message message, Class<? extends AppCompatActivity> contentActivityClass){

        NotificationUtil.sendNotification(context, message, contentActivityClass);
    }


}
