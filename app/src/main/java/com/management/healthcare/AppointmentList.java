package com.management.healthcare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppointmentList extends Fragment {
    private String UID;
    private RecyclerView recyclerView;
    private List<Appointment> combinedAppointments;
    private AppointmentListViewModel combinedApptList;
    private boolean isDoc;


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

        combinedAppointments = new ArrayList<>();
        combinedApptList = new ViewModelProvider(getActivity()).get(AppointmentListViewModel.class);
        recyclerView = view.findViewById(R.id.appointmentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user!=null){
            UID = current_user.getUid().trim();
        }else{
            Log.d("TAG", "onCreateView: UID Not found");
        }

        Bundle amIDoc = getArguments();
        if(amIDoc != null){
            String isDoctor = amIDoc.getString("doc");
            String isUser = amIDoc.getString("user");
            if(isDoctor != null){
                isDoc = true;
            }else if(isUser != null){
                isDoc = false;
            }else{
                Log.d("User or Doc", "neither!");
            }
        }

        combinedApptList.getAppointments().observe(getViewLifecycleOwner(), combinedAppointments -> {
            AppointmentAdapter adapter = new AppointmentAdapter(combinedAppointments, isDoc);
            recyclerView.setAdapter(adapter);
        });

        return view;
    }
}