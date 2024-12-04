package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationVModel extends BaseViewModel<Notification> {
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<List<Notification>>();

    @Override
    public void setModel(List<Notification> rms) {
        notifications.setValue(rms);
    }

    @Override
    public LiveData<List<Notification>> getModel() {
        if (notifications.getValue() == null) {
            notifications.setValue(new ArrayList<>());
        }
        return notifications;
    }
}
