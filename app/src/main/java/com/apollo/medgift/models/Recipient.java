package com.apollo.medgift.models;

import java.io.Serializable;

public class Recipient extends User  implements Serializable {
    public static final transient String MODEL_NAME="Recipient";
    private String address;
    private String createdBy;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
