package com.management.healthcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class HomePageFrag extends Fragment {
    private String userName;
    private String UID;
    private TextView upcomingDocName;
    private TextView upcomingDocAddr;
    private TextView upcomingDateTime;
    private TextView upcomingDocSpecial;
    private String upcomingApptDocAuthId;
    private String upcomingApptDocName;
    private String upcomingApptDate;
    private String upcomingApptTime;
    private String upcomingApptDocSpecial;
    private String upcomingApptDocAddr;
    private Appointment upcomingAppt;

    public HomePageFrag(){

    }


    TextView Greeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        upcomingDocName = view.findViewById(R.id.doctorName);
        upcomingDateTime = view.findViewById(R.id.apptTime);
        upcomingDocAddr = view.findViewById(R.id.docAddress);
        upcomingDocSpecial = view.findViewById(R.id.docSpecialization);

        Button createNewAppointment = view.findViewById(R.id.createNewAppointment);
        createNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CreateAppointment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        Button signOutButton = view.findViewById(R.id.signOutButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            UID = user.getUid();
            Log.d("UID", UID);
        }else{
            Log.d("User", "User is null");
        }

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent backToLoginRegister = new Intent(getActivity(), LoginRegister.class);
                startActivity(backToLoginRegister);
            }
        });

        Greeting = view.findViewById(R.id.dynamicGreeting);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Greeting.setText("Loading...");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.child("full_name").getValue(String.class);
                    if(userName!=null){
                        String greeting = null;
                        if(hour>= 12 && hour < 16){
                            greeting = "Good Afternoon " + userName;
                        } else if(hour >= 16 && hour < 21){
                            greeting = "Good Evening " + userName;
                        } else if(hour >= 21 && hour < 24){
                            greeting = "Good Evening "+ userName;
                        } else {
                            greeting = "Good Morning "+ userName;
                        }
                        Greeting.setText(greeting);
                    }else{
                        Greeting.setText("Error");
                    }
                } else {
                    Log.d("TAG", "No path");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Cancelled", "Query was cancelled");
            }
        });

        Button viewScheduleButton = view.findViewById(R.id.viewSchedule);
        viewScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AppointmentList())
                        .commit();
            }
        });

//        DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference("Appointments");
//        Query query = apptRef.orderByChild("dateTime").limitToFirst(1);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    DataSnapshot firstAppointmentSnapshot = snapshot.getChildren().iterator().next();
//                    upcomingAppt = firstAppointmentSnapshot.getValue(Appointment.class);
//
//                    if(upcomingAppt != null){
//                        upcomingApptDocAuthId = upcomingAppt.getDoctorAuthId();
//                        upcomingApptDocName = upcomingAppt.getDoctorName();
//                        upcomingDocName.setText(upcomingApptDocName);
//                    }else{
//                        Log.d("UpcomingApptDocAuthId", "Appt doc Id not found");
//                    }
//                    DatabaseReference docRef = FirebaseDatabase.getInstance().getReference("Doctors");
//                    docRef.child(upcomingApptDocAuthId).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                upcomingApptDocSpecial = snapshot.child("specialization").getValue().toString();
//                                upcomingApptDocAddr = snapshot.child("clinic_addr").getValue().toString();
//                                upcomingDocSpecial.setText(upcomingApptDocSpecial);
//                                upcomingDocAddr.setText(upcomingApptDocAddr);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                    Date apptDate = upcomingAppt.getDateTime();
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(apptDate);
//                    int year = calendar.get(Calendar.YEAR);
//                    int month = calendar.get(Calendar.MONTH) + 1;
//                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//                    String monthStr = "MON";
//                    if(month == 1)
//                        monthStr = "JAN";
//                    if(month == 2)
//                        monthStr = "FEB";
//                    if(month == 3)
//                        monthStr = "MAR";
//                    if(month == 4)
//                        monthStr = "APR";
//                    if(month == 5)
//                        monthStr = "MAY";
//                    if(month == 6)
//                        monthStr = "JUN";
//                    if(month == 7)
//                        monthStr = "JUL";
//                    if(month == 8)
//                        monthStr = "AUG";
//                    if(month == 9)
//                        monthStr = "SEP";
//                    if(month == 10)
//                        monthStr = "OCT";
//                    if(month == 11)
//                        monthStr = "NOV";
//                    if(month == 12)
//                        monthStr = "DEC";
//                    String date = dayOfMonth + " " + monthStr;
//                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                    int minute = calendar.get(Calendar.MINUTE);
//                    int second = calendar.get(Calendar.SECOND);
//                    String time = hour + ":" + minute;
//                    String dateTime = date + " " + time;
//                    upcomingDateTime.setText(dateTime);
//                }else{
//                    Log.d("Upcoming Appointment", "Appointment not found");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("query", "query cancelled");
//            }
//        });





        // Inflate the layout for this fragment
        return view;
    }
}