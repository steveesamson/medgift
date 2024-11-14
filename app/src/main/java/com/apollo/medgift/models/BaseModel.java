package com.apollo.medgift.models;

import java.io.Serializable;

public class BaseModel implements Serializable {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    protected String key;
}

