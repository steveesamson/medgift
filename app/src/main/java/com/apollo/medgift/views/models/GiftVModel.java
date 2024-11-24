package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.Gift;


import java.util.ArrayList;
import java.util.List;

public class GiftVModel extends BaseViewModel<Gift> {
    private final MutableLiveData<List<Gift>> gifts = new MutableLiveData<List<Gift>>();

    @Override
    public void setModel(List<Gift> rms) {
        gifts.setValue(rms);
    }

    @Override
    public LiveData<List<Gift>> getModel() {
        if (gifts.getValue() == null) {
            gifts.setValue(new ArrayList<>());
        }
        return gifts;
    }
}
