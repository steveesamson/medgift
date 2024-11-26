package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceVModel extends BaseViewModel<Service> {
    private final MutableLiveData<List<Service>> services = new MutableLiveData<List<Service>>();

    @Override
    public void setModel(List<Service> rms) {
        services.setValue(rms);
    }

    @Override
    public LiveData<List<Service>> getModel() {
        if (services.getValue() == null) {
            services.setValue(new ArrayList<>());
        }
        return services;
    }
}
