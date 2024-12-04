package com.apollo.medgift.jobs;

import android.content.Context;

import com.apollo.medgift.common.BaseJob;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Message;
import com.apollo.medgift.models.NotificationType;
import com.apollo.medgift.views.AlertDetail;

public class Delivery extends BaseJob {
    public Delivery(Context context, GiftService giftService) {
        super(context, giftService);
    }

    @Override
    public void run() {
        String title = "Delivery Due";
        String body = String.format("Service delivery, '%s' for recipient %s is due now.", this.giftService.getServiceName(),this.giftService.getRecipientName());
        Message m = new Message();
        m.setTitle(title);
        m.setBody(body);
        m.setNotificationType(NotificationType.GiftService);
        m.setButtonLabel("Service details");
        m.setPayLoad(this.giftService);
        sendNotification(m, AlertDetail.class);
    }
}
