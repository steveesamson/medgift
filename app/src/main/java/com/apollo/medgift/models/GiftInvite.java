package com.apollo.medgift.models;

import java.io.Serializable;

public class GiftInvite implements Serializable {
    private String key;
    private String giftId;
    private String inviteeId;
    private String message;
    private String creationDate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
