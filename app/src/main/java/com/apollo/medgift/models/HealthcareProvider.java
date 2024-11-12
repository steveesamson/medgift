package com.apollo.medgift.models;

import java.io.Serializable;

public class HealthcareProvider  extends User implements Serializable {
    private String role = "Provider";
    private String address;
    private String about;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRole() {
        return role;
    }
}
