package com.management.healthcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Timer;
import java.util.TimerTask;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        FirebaseApp.initializeApp(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Check if the user is logged in
                if (isUserLoggedIn()) {
                    // User is logged in, redirect to the homepage activity
                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                    finish();
                } else {
                    // User is not logged in, redirect to the login activity
                    startActivity(new Intent(getApplicationContext(), LoginRegister.class));
                    finish();
                }

            }
        }, 1000);


    }

    private boolean isUserLoggedIn() {
         FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
         return currentUser != null;
    }
}
