package com.management.healthcare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {

    private String userName;
    private String doctorName;
    private String doctorSpecialization;
    private String userAuthId;
    private String docAuthId;
    private String venue;
    private long dateTime;

    public Appointment() {
    }

    public Appointment(String doctorName, String userName, String doctorSpecialization, String dateTimeStr, String venue, String userAuthId, String docAuthId) {
        this.doctorName = doctorName;
        this.userName = userName;
        this.doctorSpecialization = doctorSpecialization;
        this.venue = venue;
        this.userAuthId = userAuthId;
        this.docAuthId = docAuthId;
        Date dateObj;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateObj = dateFormat.parse(dateTimeStr);
            assert dateObj != null;
            this.dateTime = dateObj.getTime();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
    }

    public String getDocAuthId() {
        return docAuthId;
    }

    public void setDocAuthId(String docAuthId) {
        this.docAuthId = docAuthId;
    }

    public boolean isPassed() {
        // Get the current date and time
        Date currentDate = new Date();
        long currentDateTime = currentDate.getTime();

        // Compare the appointment date with the current date
        return this.dateTime <= currentDateTime;
    }
}
