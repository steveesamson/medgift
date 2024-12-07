package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

public class Gift extends BaseModel {
    public static final String STORE ="Gift";
    private String name;
    private String recipientId;
    private String description;
    private int isGroup = 0; // 0 = false, 1 = true
    private String createdBy;

    public Gift(){}
    public Gift(String name, String recipientId, String description){
        this.name = name;
        this.recipientId =recipientId;
        this.description  = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsGroup() {
        return isGroup == 1;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup ? 1 : 0;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
