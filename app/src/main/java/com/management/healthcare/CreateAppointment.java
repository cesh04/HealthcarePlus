package com.management.healthcare;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateAppointment extends Fragment {

    private Button enterDateTime;
    private AutoCompleteTextView enterDocName;
    private ArrayAdapter<String> adapter;
    private Button createAppointment;
    private AppointmentListViewModel combinedApptList;
    private List<Appointment> combinedAppointments;
    String docName;
    String dateTimeStr;
    String userAuthId;
    String userName;
    String userPhone;
    String doctorAuthId;
    String selectedDocName;
    String selectedDocSpecial;
    String selectedDocClinicPhone;
    String selectedDocClinicAddr;
    private boolean validated;


    public CreateAppointment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);
        enterDocName = view.findViewById(R.id.enterDocName);
        enterDateTime = view.findViewById(R.id.enterDateTime);
        createAppointment = view.findViewById(R.id.createNewAppointment);
        combinedAppointments = new ArrayList<>();
        combinedApptList = new ViewModelProvider(getActivity()).get(AppointmentListViewModel.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            userAuthId = user.getUid();
            Log.d("userAuthId", userAuthId);
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userAuthId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userName = snapshot.child("full_name").getValue(String.class);
                    userPhone = snapshot.child("mobile").getValue(String.class);
                }else{
                    Log.d("userName in CreateAppointment", "userName not accessed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("userName in CreateAppointment", "userName query cancelled");
            }
        });



        enterDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog();
            }
        });

        DatabaseReference docRef = FirebaseDatabase.getInstance().getReference("Doctors");
        docRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> docNames = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String selectedDocName = dataSnapshot.child("full_name").getValue(String.class);
                    //String doctorAuthId = dataSnapshot.getKey();
                    String docSpecial = dataSnapshot.child("specialization").getValue(String.class);
                    String docClinicPhone = dataSnapshot.child("clinic_phone").getValue(String.class);
                    String docClinicAddr = dataSnapshot.child("clinic_addr").getValue(String.class);
                    String doc = selectedDocName + ", " + docSpecial + ", " + docClinicPhone + ", " + docClinicAddr;
                    if(selectedDocName != null){
                        docNames.add(doc);
                    }
                }
                adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, docNames);
                enterDocName.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("autocomplete", "error finding docName");
            }
        });

        enterDocName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDoc = (String) parent.getItemAtPosition(position);
                String[] doc = selectedDoc.split(", ");
                selectedDocName = doc[0].trim();
                selectedDocSpecial = doc[1];
                selectedDocClinicPhone = doc[2];
                selectedDocClinicAddr = "";
                for(int i=3;i<doc.length-1;i++){
                    selectedDocClinicAddr = selectedDocClinicAddr.concat(doc[i] + ", ");
                }
                selectedDocClinicAddr = selectedDocClinicAddr.concat(doc[doc.length-1]);
                //selectedDocClinicAddr = doc[2] + doc[3];
                if(selectedDocName!=null){
                    DatabaseReference docRef2 = FirebaseDatabase.getInstance().getReference("Doctors");
                    Query query = docRef2.orderByChild("full_name").equalTo(selectedDocName+"").limitToFirst(1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                doctorAuthId = dataSnapshot.getKey();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("docAuthId", "cancelled");
                        }
                    });
                }
            }
        });

        createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateAll()){
                    String apptId = UUID.randomUUID().toString();
                    Appointment appt = new Appointment(apptId, selectedDocName, selectedDocClinicPhone,  userName, userPhone, selectedDocSpecial, dateTimeStr, selectedDocClinicAddr,userAuthId, doctorAuthId);
                    Log.d("Appointment", appt.getDoctorName() + " " + appt.getDoctorSpecialization() + " " + appt.getVenue() + " " + appt.getDateTime());
                    if(appt.isPassed()){
                        Toast.makeText(getActivity(), "Invalid date and time setting", Toast.LENGTH_LONG).show();
                    } else {
                        DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference().child("Appointments");
                        apptRef.child(apptId).setValue(appt)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Appointment scheduled!", Toast.LENGTH_LONG).show();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, new HomePageFrag())
                                            .commit();
                                })
                                .addOnFailureListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Appointment creation failed!", Toast.LENGTH_LONG).show();
                                });
                    }
                }else{
                    Toast.makeText(getActivity(), "Enter all details", Toast.LENGTH_LONG).show();
                    Log.d("docAuthId", "insufficient input");
                }
            }
        });

        return view;
    }

//    private void validateTime(){
//        long timestamp;
//        try {
//            Date dateObj;
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            dateObj = dateFormat.parse(dateTimeStr);
//            assert dateObj != null;
//            timestamp = dateObj.getTime();
//            // code for checking timestamp value of existing appointments with selectedDocName;
//            DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference("Appointments");
//            Query query = apptRef.orderByChild("doctorName").equalTo(selectedDocName);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        Appointment appointment = dataSnapshot.getValue(Appointment.class);
//                        if(timestamp >= appointment.getDateTime() && timestamp < (appointment.getDateTime()+1800000)){
//                            Toast.makeText(getActivity(), "Time slot is booked for "+appointment.getDoctorName(), Toast.LENGTH_SHORT).show();
//                            validated = false;
//                            break;
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    private boolean validateAll(){
        validated = true;
        if(doctorAuthId==null || selectedDocClinicPhone==null || selectedDocSpecial==null || selectedDocName==null || userAuthId==null || dateTimeStr==null || userName==null){
            return false;
        }

        return validated;
    }

    private void showDateTimeDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog dateDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr = String.format("%02d", month + 1);
                        String dayOfMonthStr = String.format("%02d", dayOfMonth);
                        dateTimeStr = year + "-" + monthStr + "-" + dayOfMonthStr;

                        TimePickerDialog timeDialog = new TimePickerDialog(
                                requireContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        String hourOfDayStr = String.format("%02d", hourOfDay);
                                        String minuteStr = String.format("%02d", minute);
                                        dateTimeStr += " " + hourOfDayStr + ":" + minuteStr;
                                        enterDateTime.setText(dateTimeStr);
                                    }
                                },
                                hourOfDay,
                                minute,
                                true
                        );
                        timeDialog.show();
                    }
                },
                year,
                month,
                dayOfMonth
        );
        dateDialog.show();
    }
}
