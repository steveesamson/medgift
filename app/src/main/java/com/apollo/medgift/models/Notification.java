package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

public class Notification extends BaseModel {
    public static final String STORE ="Notification";

    private String userId;
    private String giftServiceId;
    private String message;
    private String status = NotificationStatus.PENDING;
    private String creationDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGiftServiceId() {
        return giftServiceId;
    }

    public void setGiftServiceId(String giftServiceId) {
        this.giftServiceId = giftServiceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
