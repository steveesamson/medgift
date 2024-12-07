package com.apollo.medgift.jobs;

import android.content.Context;

import com.apollo.medgift.common.BaseJob;
import com.apollo.medgift.common.NotificationUtil;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.models.NotificationType;

public class WatchDelivery extends BaseJob {

    public WatchDelivery(Context context, GiftService giftService, String createdFor) {
        super(context, giftService, createdFor);
    }

    @Override
    public void run() {
        String title = "Delivery Due Soon";
        String body = String.format("Service delivery: '%s' for recipient '%s' is due in 3 hours.", this.giftService.getServiceName(),this.giftService.getRecipientName());
        Message m = new Message();
        m.setTitle(title);
        m.setBody(body);
        m.setNotificationType(NotificationType.GiftService);
        m.setButtonLabel("Service details");
        m.setPayLoad(this.giftService);
        NotificationUtil.saveNotification(m, this.createdFor);
        sendNotification(m);
    }
}
