package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.Availability;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityVModel extends BaseViewModel<Availability> {
    private final MutableLiveData<List<Availability>> gifts = new MutableLiveData<List<Availability>>();

    @Override
    public void setModel(List<Availability> rms) {
        gifts.setValue(rms);
    }

    @Override
    public LiveData<List<Availability>> getModel() {
        if (gifts.getValue() == null) {
            gifts.setValue(new ArrayList<>());
        }
        return gifts;
    }

}
