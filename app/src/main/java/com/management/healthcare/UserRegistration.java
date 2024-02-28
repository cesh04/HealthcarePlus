package com.management.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UserRegistration extends AppCompatActivity
{
    private EditText user_full_name;
    private EditText user_email;
    private EditText user_mobile;
    private EditText user_address;
    private long birthdate_timestamp;
    private EditText user_password;
    private EditText confirm_password;
    Button registerButton;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private RadioGroup genderRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private String user_gender;
    private FirebaseAuth mAuth;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        user_full_name = findViewById(R.id.userRegName);
        user_email = findViewById(R.id.userRegEmail);
        user_mobile = findViewById(R.id.userRegMobile);
        user_address = findViewById(R.id.userRegAddress);
        genderRadioGroup = findViewById(R.id.radioBtnGroup);
        user_password = findViewById(R.id.userRegPassword);
        confirm_password = findViewById(R.id.userRegConfirmPassword);
        mAuth = FirebaseAuth.getInstance();


        registerButton = findViewById(R.id.UserRegButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString().trim();
                String password = user_password.getText().toString().trim();

                selectedButtonId = genderRadioGroup.getCheckedRadioButtonId();
                if(selectedButtonId != -1){
                    RadioButton selectedRadioButton = findViewById(selectedButtonId);
                    user_gender = selectedRadioButton.getText().toString().trim();
                }


                if(validateAllFields()){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        if(current_user != null){
                                            UID = current_user.getUid();

                                            registerToDatabase();
                                        }

                                    }else{
                                        if(task.getException() != null && task.getException().getMessage() != null){
                                            String errorMessage = task.getException().getMessage();
                                            if(errorMessage.contains("email address is already in use")){
                                                Toast.makeText(UserRegistration.this, "Email is already in use", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(UserRegistration.this, "Registration failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            });


                }else{
                    Toast.makeText(UserRegistration.this, "Validation error", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private boolean validateAllFields(){
        String full_name = user_full_name.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        String mobile = user_mobile.getText().toString().trim();
        String address = user_address.getText().toString().trim();
        String password = user_password.getText().toString().trim();
        String conf_password = confirm_password.getText().toString().trim();

        if(TextUtils.isEmpty(full_name)){
            Toast.makeText(UserRegistration.this, "Please enter your name", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(UserRegistration.this, "Please enter your email", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(mobile) || mobile.length() != 10){
            Toast.makeText(UserRegistration.this, "Please enter your mobile", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(address)){
            Toast.makeText(UserRegistration.this, "Please enter your address", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(user_gender)){
            Toast.makeText(UserRegistration.this, "Please select your gender", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(UserRegistration.this, "Please enter your password", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(conf_password) || conf_password.length() < 8){
            Toast.makeText(UserRegistration.this, "Please confirm your password", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(TextUtils.equals(password,conf_password))){
            Toast.makeText(UserRegistration.this, "Password fields don't match!", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    private void registerToDatabase(){
        String full_name = user_full_name.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        String mobile = user_mobile.getText().toString().trim();
        String address = user_address.getText().toString().trim();
        String gender = user_gender;
        long timestamp = birthdate_timestamp;
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(full_name, email, mobile, address, timestamp, gender);
        usersRef.child(UID).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserRegistration.this, "Registration finally successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserRegistration.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                });

        Intent intentUser = new Intent(UserRegistration.this,LoginRegister.class);
        startActivity(intentUser);
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);

                Calendar birthdate = Calendar.getInstance();
                birthdate.set(year, month-1, day);
                birthdate_timestamp = birthdate.getTimeInMillis();
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);



        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }




}