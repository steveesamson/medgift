package com.apollo.medgift.views.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apollo.medgift.common.BaseViewModel;
import com.apollo.medgift.models.GiftInvite;

import java.util.ArrayList;
import java.util.List;

public class GiftInviteVModel extends BaseViewModel<GiftInvite> {
    private final MutableLiveData<List<GiftInvite>> giftInvitees = new MutableLiveData<List<GiftInvite>>();

    @Override
    public void setModel(List<GiftInvite> rms) {
        giftInvitees.setValue(rms);
    }

    @Override
    public LiveData<List<GiftInvite>> getModel() {
        if (giftInvitees.getValue() == null) {
            giftInvitees.setValue(new ArrayList<>());
        }
        return giftInvitees;
    }
}
