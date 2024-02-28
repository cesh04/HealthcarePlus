package com.management.healthcare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentList extends Fragment {
    private String UID;
    private RecyclerView recyclerView;


    public AppointmentList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        recyclerView = view.findViewById(R.id.appointmentRecyclerView);
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user!=null){
            UID = current_user.getUid().trim();
        }else{
            Log.d("TAG", "onCreateView: UID Not found");
        }
        DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference().child("Appointments");
        Query query = apptRef.orderByChild("userAuthId").equalTo(UID+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> appointments = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);
                    if(appointment!=null){
                        appointments.add(appointment);
                    }
                }

                AppointmentAdapter adapter = new AppointmentAdapter(appointments);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("userAppt", "user appointment query cancelled");
            }
        });

        return view;
    }
}