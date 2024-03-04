package com.management.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    private AppointmentListViewModel upcomingApptList;
    private AppointmentListViewModel passedApptList;
    private String UID;

    private String userName;
    private String doctorName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        upcomingApptList = new ViewModelProvider(this).get(AppointmentListViewModel.class);
        passedApptList = new ViewModelProvider(this).get(AppointmentListViewModel.class);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            UID = user.getUid();
        }
        else{
            Log.d("User","User is null");
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userName = snapshot.child("full_name").getValue(String.class);
                    Log.d("HomePage",userName);
                    HomePageFrag homePageFrag = new HomePageFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", userName);
                    homePageFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, homePageFrag)
                            .addToBackStack(null)
                            .commit();
                    goToUserHomePage();
                }else{
                    Log.d("User ID", "Not user. Are you a doctor?");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference docRef = FirebaseDatabase.getInstance().getReference("Doctors").child(UID);
        docRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    doctorName = snapshot.child("full_name").getValue(String.class);
                    Log.d("HomePage",doctorName);
                    DocHomePageFrag docHomePageFrag = new DocHomePageFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("doctorName", doctorName);
                    docHomePageFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, docHomePageFrag)
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void goToUserHomePage(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("full_name");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("userName","Username query failed!");
            }
        });
    }
}