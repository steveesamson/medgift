package com.apollo.medgift.models;


import com.apollo.medgift.common.BaseModel;

public class User extends BaseModel {
    public static final String STORE ="User";
    public enum Role {
        PROVIDER,
        GIFTER
    }
    private String role = "Gifter";// Gifter/Provider
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String phone;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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




}
