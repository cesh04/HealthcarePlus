package com.management.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class doctor_registration extends AppCompatActivity {


    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        btn1 = findViewById(R.id.docRegNextPgBtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentUser = new Intent(doctor_registration.this,Doctor_Clinic_Page.class);
                startActivity(intentUser);
            }
        });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Spinner spinner = (Spinner) findViewById(R.id.docRegSpecial);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.doc_specialization,
                android.R.layout.simple_spinner_item
        );


// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

    }


}