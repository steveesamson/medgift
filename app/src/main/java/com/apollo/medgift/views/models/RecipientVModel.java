package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.models.Recipient;

import java.util.ArrayList;
import java.util.List;

public class RecipientVModel extends  BaseViewModel<Recipient> {
    private final MutableLiveData<List<Recipient>> recipients = new MutableLiveData<List<Recipient>>();

    @Override
    public void setModel(List<Recipient> rms) {
      recipients.setValue(rms);
    }

    @Override
    public LiveData<List<Recipient>> getModel() {
        if (recipients.getValue() == null) {
            recipients.setValue(new ArrayList<>());
        }
        return recipients;
    }
}
