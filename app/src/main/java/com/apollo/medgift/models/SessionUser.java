package com.apollo.medgift.models;

import com.google.firebase.auth.FirebaseUser;

public class SessionUser {
    private String userId;
    private String userRole;
    private String userName;

    public  SessionUser(FirebaseUser user){
        String[] nameRole = user.getDisplayName().split("\\|");
        userId = user.getUid();
        userName = nameRole[0];
        userRole = nameRole[1];
    }

    public String getUserId() {
        return userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserName() {
        return userName;
    }
}
