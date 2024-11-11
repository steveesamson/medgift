package com.apollo.medgift.models;

public class Notification {
    private String key;
    private String userId;
    private String giftServiceId;
    private String message;
    private String status = NotificationStatus.PENDING;
    private String creationDate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
