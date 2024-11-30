package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.DayTime;
import com.apollo.medgift.models.Gift;

import java.util.ArrayList;
import java.util.List;

public class DayTimeVModel extends BaseViewModel<DayTime> {
    private final MutableLiveData<List<DayTime>> gifts = new MutableLiveData<List<DayTime>>();

    @Override
    public void setModel(List<DayTime> rms) {
        gifts.setValue(rms);
    }

    @Override
    public LiveData<List<DayTime>> getModel() {
        if (gifts.getValue() == null) {
            gifts.setValue(new ArrayList<>());
        }
        return gifts;
    }

    @Override
    public void addModel(DayTime model) {
        if (gifts.getValue() == null) {
            gifts.setValue(new ArrayList<>());
        }
        gifts.getValue().add(model);
    }
}
