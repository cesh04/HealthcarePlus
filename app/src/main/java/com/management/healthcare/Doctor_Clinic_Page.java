package com.management.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Doctor_Clinic_Page extends AppCompatActivity {

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_clinic_page);

        btn1 = findViewById(R.id.docRegConfirmBtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentUser = new Intent(Doctor_Clinic_Page.this,HomePage.class);
                startActivity(intentUser);
            }
        });
    }
}