package com.management.healthcare;

public class User {
    private String full_name;
    private String email;
    private String mobile;
    private String address;
    private long birthdate_timestamp;
    private String gender;

    public User(){

    }

    public User(String full_name, String email, String mobile, String address, long birthdate_timestamp, String gender){
        this.full_name = full_name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.gender = gender;
        this.birthdate_timestamp = birthdate_timestamp;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBirthdate_timestamp() {
        return birthdate_timestamp;
    }

    public void setBirthdate_timestamp(long birthdate_timestamp) {
        this.birthdate_timestamp = birthdate_timestamp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
