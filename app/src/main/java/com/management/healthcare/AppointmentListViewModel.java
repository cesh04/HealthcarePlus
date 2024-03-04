package com.management.healthcare;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListViewModel extends ViewModel {
    private MutableLiveData<List<Appointment>> appointments = new MutableLiveData<>();

    public MutableLiveData<List<Appointment>> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments.setValue(appointments);
    }
}
