package com.apollo.medgift.models;

import androidx.annotation.NonNull;

import com.apollo.medgift.common.BaseModel;

public class GiftInvite extends BaseModel {
    public static final String STORE ="GiftInvite";

    private String giftId;
    private String inviteeId;
    private String name;
    private String creationDate;
    private String status = InviteStatus.PENDING;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(String inviteeId) {
        this.inviteeId = inviteeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString(){
        return name;
    }
}
