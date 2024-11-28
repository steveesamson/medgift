package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.GiftService;

import java.util.ArrayList;
import java.util.List;

public class GiftServiceVModel extends BaseViewModel<GiftService> {
    private final MutableLiveData<List<GiftService>> giftServices = new MutableLiveData<List<GiftService>>();

    @Override
    public void setModel(List<GiftService> rms) {
        giftServices.setValue(rms);
    }

    @Override
    public LiveData<List<GiftService>> getModel() {
        if (giftServices.getValue() == null) {
            giftServices.setValue(new ArrayList<>());
        }
        return giftServices;
    }
}
