package com.apollo.medgift.models;

import androidx.annotation.NonNull;

import com.apollo.medgift.common.BaseModel;

public class GiftInvite extends BaseModel {
    public static final String STORE ="GiftInvite";

    private String giftId;
    private String email;
    private String name;
    private String creationDate;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString(){
        return name;
//        return String.format("%s", name, email);
    }
}
