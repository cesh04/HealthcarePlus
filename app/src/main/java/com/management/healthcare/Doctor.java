package com.management.healthcare;

public class Doctor {
    private String full_name;
    private String mobile;
    private String email;
    private String specialization;
    private String gender;
    private String clinic_addr;
    private String clinic_phone;


    public Doctor() {
    }

    public Doctor(String full_name, String mobile, String email, String specialization, String gender, String clinic_addr, String clinic_phone) {
        this.full_name = full_name;
        this.mobile = mobile;
        this.email = email;
        this.specialization = specialization;
        this.gender = gender;
        this.clinic_addr = clinic_addr;
        this.clinic_phone = clinic_phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClinic_addr() {
        return clinic_addr;
    }

    public void setClinic_addr(String clinic_addr) {
        this.clinic_addr = clinic_addr;
    }

    public String getClinic_phone() {
        return clinic_phone;
    }

    public void setClinic_phone(String clinic_phone) {
        this.clinic_phone = clinic_phone;
    }
}
