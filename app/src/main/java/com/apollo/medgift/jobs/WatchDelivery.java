package com.apollo.medgift.jobs;

import android.content.Context;

import com.apollo.medgift.common.BaseJob;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.models.NotificationType;
import com.apollo.medgift.views.AlertDetail;

public class WatchDelivery extends BaseJob {
    public WatchDelivery(Context context, GiftService giftService) {
        super(context, giftService);
    }

    @Override
    public void run() {
        String title = "Delivery Due Soon";
        String body = String.format("Service delivery, '%s' for recipient %s is due in 3 hours.", this.giftService.getServiceName(),this.giftService.getRecipientName());
        Message m = new Message();
        m.setTitle(title);
        m.setBody(body);
        m.setNotificationType(NotificationType.GiftService);
        m.setButtonLabel("See details");
        m.setTargetKey(this.giftService.getKey());
        sendNotification(m, AlertDetail.class);
    }
}
