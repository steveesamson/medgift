package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

import java.io.Serializable;

public class Message implements Serializable {
    public static final String STORE = "Message";
    private String payloadKey;
    private String title;
    private String body;

    private BaseModel payLoad;
    private String buttonLabel;
    private NotificationType notificationType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public BaseModel getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(BaseModel payLoad) {
        this.payLoad = payLoad;
    }
    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getPayloadKey() {
        return payloadKey;
    }

    public void setPayloadKey(String payloadKey) {
        this.payloadKey = payloadKey;
    }
}
