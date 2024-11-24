package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.HealthTip;

import java.util.ArrayList;
import java.util.List;

public class HealthtipVModel extends BaseViewModel<HealthTip> {
    private final MutableLiveData<List<HealthTip>> healthtips = new MutableLiveData<List<HealthTip>>();

    @Override
    public void setModel(List<HealthTip> rms) {
        healthtips.setValue(rms);
    }

    @Override
    public LiveData<List<HealthTip>> getModel() {
        if (healthtips.getValue() == null) {
            healthtips.setValue(new ArrayList<>());
        }
        return healthtips;
    }
}
