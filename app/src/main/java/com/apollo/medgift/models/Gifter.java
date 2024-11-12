package com.apollo.medgift.models;

import java.io.Serializable;

public class Gifter extends User implements Serializable {
    private final String role = "Gifter";

    public String getRole() {
        return role;
    }

}
