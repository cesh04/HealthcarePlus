package com.management.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRegister extends AppCompatActivity {

    Button mainPgRegBtn,loginPageUserReg,loginPageDocReg,mainLoginButton;
    private EditText emailField;
    private EditText passwordField;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        mainPgRegBtn = findViewById(R.id.mainPageRegButton);
        loginPageUserReg = findViewById(R.id.loginPageUserReg);
        loginPageDocReg = findViewById(R.id.loginPageDocReg);
        mainLoginButton = findViewById(R.id.mainLoginButton);
        emailField = findViewById(R.id.loginEmailTextField);
        passwordField = findViewById(R.id.loginPasswordTextField);

        mAuth = FirebaseAuth.getInstance();


        mainPgRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginPageUserReg.setVisibility(View.VISIBLE);
                loginPageDocReg.setVisibility(View.VISIBLE);
            }
        });

        loginPageUserReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentUser = new Intent(LoginRegister.this,UserRegistration.class);
                startActivity(intentUser);
            }
        });

        loginPageDocReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentUser = new Intent(LoginRegister.this,DocRegister.class);
                startActivity(intentUser);
            }
        });

        mainLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailField.getText().toString().trim();
                password = passwordField.getText().toString().trim();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginRegister.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginRegister.this, "Login failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }
}