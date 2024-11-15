package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

public class HealthTip extends BaseModel {
    public static final String STORE ="HealthTip";

    private String title;
    private  String content;
    private String creationDate;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
