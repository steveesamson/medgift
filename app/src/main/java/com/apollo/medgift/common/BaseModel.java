package com.apollo.medgift.common;

import java.io.Serializable;

public class BaseModel implements Serializable {
    public final static String STORE = "Base";
    protected String key = "";
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
