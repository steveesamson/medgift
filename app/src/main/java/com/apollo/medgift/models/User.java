package com.apollo.medgift.models;


import androidx.annotation.NonNull;

import com.apollo.medgift.common.BaseModel;

public class User extends BaseModel {
    public static final String STORE ="User";

    public enum Type {
        PROVIDER,
        GIFTER
    }
    private String role;// Gifter/Provider
    private String firstName;
    private String lastName;
    private String email;

    public User(){
        this.role = Type.GIFTER.name();
    }
    public User(Type role){
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

    public void setRole(Type role) {
        this.role = role.name();
    }

    @NonNull
    @Override
    public String toString(){
        return String.format("%s %s <%s>", firstName, lastName, email);
    }


}
