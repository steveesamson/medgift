package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.HealthcareService;

import java.util.ArrayList;
import java.util.List;

public class ServiceVModel extends BaseViewModel<HealthcareService> {
    private final MutableLiveData<List<HealthcareService>> healthcareServices = new MutableLiveData<List<HealthcareService>>();

    @Override
    public void setModel(List<HealthcareService> rms) {
        healthcareServices.setValue(rms);
    }

    @Override
    public LiveData<List<HealthcareService>> getModel() {
        if (healthcareServices.getValue() == null) {
            healthcareServices.setValue(new ArrayList<>());
        }
        return healthcareServices;
    }
}
