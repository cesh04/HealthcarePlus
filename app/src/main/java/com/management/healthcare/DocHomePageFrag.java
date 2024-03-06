package com.management.healthcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DocHomePageFrag extends Fragment {
    private String doctorName;
    private String UID;
    private TextView upcomingUserName;
    private TextView upcomingDocAddr;
    private TextView upcomingDateTime;
    private TextView upcomingDocSpecial;
    private String upcomingApptUserName;
    private String upcomingApptDocSpecial;
    private String upcomingApptDocAddr;
    private Appointment upcomingAppt;
    TextView Greeting;
    private List<Appointment> appointments;
    private List<Appointment> passedAppointments;
    private List<Appointment> combinedAppointments;
    private AppointmentListViewModel combinedApptList;

    public DocHomePageFrag(){

    }
    public void onSaveInstanceState(Bundle outState){
        String greetingName = Greeting.getText().toString().trim();
        String userName = upcomingUserName.getText().toString().trim();
        String docAddr = upcomingDocAddr.getText().toString().trim();
        String dateTime = upcomingDateTime.getText().toString().trim();
        String docSpecial = upcomingDocSpecial.getText().toString().trim();

        outState.putString("greetingName", greetingName);
        outState.putString("userName", userName);
        outState.putString("docAddr", docAddr);
        outState.putString("dateTime", dateTime);
        outState.putString("docSpecial", docSpecial);
    }

    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            Greeting.setText(savedInstanceState.getString("greetingName"));
            upcomingUserName.setText(savedInstanceState.getString("userName"));
            upcomingDocAddr.setText(savedInstanceState.getString("docAddr"));
            upcomingDateTime.setText(savedInstanceState.getString("dateTime"));
            upcomingDocSpecial.setText(savedInstanceState.getString("docSpecial "));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_home_page, container, false);
        upcomingUserName = view.findViewById(R.id.userName);
        upcomingDateTime = view.findViewById(R.id.apptTime);
        upcomingDocAddr = view.findViewById(R.id.docAddress);
        upcomingDocSpecial = view.findViewById(R.id.docSpecialization);
        Greeting = view.findViewById(R.id.dynamicGreeting);
        appointments = new ArrayList<>();
        passedAppointments = new ArrayList<>();
        combinedAppointments = new ArrayList<>();
        combinedApptList = new ViewModelProvider(getActivity()).get(AppointmentListViewModel.class);

        Date dateObj = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObj);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        Greeting.setText("Loading...");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            UID = user.getUid();
            Log.d("UID", UID);
        }else{
            Log.d("User", "User is null");
        }

        Bundle docNameBundle = getArguments();
        if(docNameBundle != null){
            doctorName = docNameBundle.getString("doctorName");
            if(doctorName!=null){
                if(doctorName!=null){
                    String greeting = null;
                    if(hour>= 12 && hour < 16){
                        greeting = "Good Afternoon " + doctorName;
                    } else if(hour >= 16 && hour < 21){
                        greeting = "Good Evening " + doctorName;
                    } else if(hour >= 21 && hour < 24){
                        greeting = "Good Evening "+ doctorName;
                    } else {
                        greeting = "Good Morning "+ doctorName;
                    }
                    Greeting.setText(greeting);
                }else{
                    Greeting.setText("Error");
                }
            }
        }else{
            Log.d("docNameBundle", "docNameBundle not found yet");
        }



        Button signOutButton = view.findViewById(R.id.signOutButton);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent backToLoginRegister = new Intent(getActivity(), LoginRegister.class);
                startActivity(backToLoginRegister);
            }
        });



        Button viewScheduleButton = view.findViewById(R.id.viewSchedule);
        viewScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppointmentList apptListFrag = new AppointmentList();
                Bundle iAmDoc = new Bundle();
                iAmDoc.putString("doc", "doc");
                apptListFrag.setArguments(iAmDoc);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, apptListFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference().child("Appointments");
        Query query = apptRef.orderByChild("docAuthId").equalTo(UID+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);

                    if(appointment!=null && !appointment.isPassed()){
                        appointments.add(appointment);
                        Log.d("appointment", appointment.getDoctorName());
                    }else{
                        passedAppointments.add(appointment);
                        Log.d("appointment query", "appointment not accessed");
                    }
                }
                if(appointments.isEmpty()){
                    Log.d("appointment local list", "appointments list is empty");
                }else{
                    sortAppointmentsByTimestamp(appointments);
                    if(!appointments.isEmpty()){
                        upcomingAppt = appointments.get(0);
                        upcomingApptUserName = upcomingAppt.getUserName();
                        upcomingApptDocAddr = upcomingAppt.getVenue();
                        upcomingApptDocSpecial = upcomingAppt.getDoctorSpecialization();

                        upcomingUserName.setText(upcomingApptUserName);
                        upcomingDocSpecial.setText(upcomingApptDocSpecial);
                        upcomingDocAddr.setText(upcomingApptDocAddr);
                        long apptDateTime = upcomingAppt.getDateTime();
                        Date apptDate = new Date(apptDateTime);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(apptDate);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        String monthStr = "MON";
                        if(month == 1)
                            monthStr = "JAN";
                        if(month == 2)
                            monthStr = "FEB";
                        if(month == 3)
                            monthStr = "MAR";
                        if(month == 4)
                            monthStr = "APR";
                        if(month == 5)
                            monthStr = "MAY";
                        if(month == 6)
                            monthStr = "JUN";
                        if(month == 7)
                            monthStr = "JUL";
                        if(month == 8)
                            monthStr = "AUG";
                        if(month == 9)
                            monthStr = "SEP";
                        if(month == 10)
                            monthStr = "OCT";
                        if(month == 11)
                            monthStr = "NOV";
                        if(month == 12)
                            monthStr = "DEC";
                        String date = dayOfMonth + " " + monthStr;
                        int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
                        String hourStr = String.format("%02d", hour2);
                        int minute = calendar.get(Calendar.MINUTE);
                        String minuteStr = String.format("%02d", minute);
                        int second = calendar.get(Calendar.SECOND);
                        String time = hourStr + ":" + minuteStr;
                        String dateTime = date + " " + time;
                        upcomingDateTime.setText(dateTime);
                        Log.d("upcoming appt", upcomingApptUserName);
                    }else{
                        Log.d("upcoming appt", "appointment list is empty outside");
                    }
                    if(passedAppointments.isEmpty()){
                        Log.d("passedAppointments", "no previous appointments");
                    }else{
                        sortAppointmentsByTimestamp(passedAppointments);
                    }
                    combinedAppointments.addAll(appointments);
                    combinedAppointments.addAll(passedAppointments);
                    combinedApptList.setAppointments(combinedAppointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("userAppt", "user appointment query cancelled");
            }
        });

        Log.d("UID3", UID);







        // Inflate the layout for this fragment
        return view;
    }

    public void sortAppointmentsByTimestamp(List<Appointment> appointments) {
        // Create a custom comparator to compare appointments by timestamp
        Comparator<Appointment> comparator = new Comparator<Appointment>() {
            @Override
            public int compare(Appointment appointment1, Appointment appointment2) {
                long timestamp1 = appointment1.getDateTime();
                long timestamp2 = appointment2.getDateTime();
                // Compare timestamps
                return Long.compare(timestamp1, timestamp2);
            }
        };

        // Sort the list of appointments using the custom comparator
        appointments.sort(comparator);
    }
}