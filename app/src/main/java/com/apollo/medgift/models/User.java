package com.apollo.medgift.models;


import com.apollo.medgift.common.BaseModel;

public class User extends BaseModel {
    public static final String STORE ="User";

    public enum Role {
        PROVIDER,
        GIFTER
    }
    private String role;// Gifter/Provider
    private String firstName;
    private String lastName;
    private String email;

    public User(Role role){
        this.role = role.name();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role.name();
    }



}
