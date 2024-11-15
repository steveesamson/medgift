package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

public class GiftInvite extends BaseModel {
    public static final String STORE ="GiftInvite";

    private String giftId;
    private String inviteeId;
    private String message;
    private String creationDate;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(String inviteeId) {
        this.inviteeId = inviteeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
