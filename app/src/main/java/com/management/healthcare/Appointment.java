package com.management.healthcare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {
    private String doctorName;
    private String doctorAuthId;
    private String userAuthId;
    private Date dateTime; // Combined date and time

    public Appointment() {
    }

    public Appointment(String doctorName, String doctorAuthId, String userAuthId, String dateTimeStr) {
        this.doctorName = doctorName;
        this.doctorAuthId = doctorAuthId;
        this.userAuthId = userAuthId;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.dateTime = dateFormat.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorAuthId() {
        return doctorAuthId;
    }

    public void setDoctorAuthId(String doctorAuthId) {
        this.doctorAuthId = doctorAuthId;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isPassed() {
        // Get the current date and time
        Date currentDate = new Date();

        // Compare the appointment date with the current date
        return this.dateTime.before(currentDate);
    }
}
