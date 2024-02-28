package com.management.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DocRegister extends AppCompatActivity implements DocInfo.OnDataPassedListener, DocClinic.OnDataPassedListener{
    private String doc_name;
    private String doc_phone;
    private String doc_email;
    private String doc_specialization;
    private String doc_gender;
    private String doc_password;
    private String doc_clinic_addr;
    private String doc_clinic_phone;
    private FirebaseAuth mAuth;
    private String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_register);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DocInfo(), "doc_info_fragment")
                .commit();

        mAuth = FirebaseAuth.getInstance();
    }

    public void onDataPassed(String doc_name, String doc_phone,
                             String doc_email, String doc_specialization,
                             String doc_gender, String doc_password){
        this.doc_name = doc_name;
        this.doc_phone = doc_phone;
        this.doc_email = doc_email;
        this.doc_specialization = doc_specialization;
        this.doc_gender = doc_gender;
        this.doc_password = doc_password;
        Log.d("DocInfo data", "Received" + doc_name);
        checkIfDocInfoReceived();
    }

    @Override
    public void onDataPassed(String doc_clinic_addr, String doc_clinic_phone) {
        this.doc_clinic_addr = doc_clinic_addr;
        this.doc_clinic_phone = doc_clinic_phone;
        Log.d("DocClinic data", "Received");
        checkIfDocInfoReceived();
    }

    private void checkIfDocInfoReceived(){
        if(doc_email != null && doc_password!= null && doc_clinic_phone!=null && doc_clinic_addr!=null){
            registerDoc();
        }
    }


    private void registerDoc(){
        mAuth.createUserWithEmailAndPassword(doc_email, doc_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
                            if(curr_user != null){
                                UID = curr_user.getUid();
                                Log.d("mAuth", "successful and db entry started");
                                registerToDatabase(doc_name, doc_phone, doc_email, doc_specialization, doc_gender,doc_password, doc_clinic_addr, doc_clinic_phone);
                            }
                        }else{
                            if(task.getException() != null && task.getException().getMessage() != null){
                                String errorMessage = task.getException().getMessage();
                                if(errorMessage.contains("email address is already in use")){
                                    Toast.makeText(DocRegister.this, "Email is already in use", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(DocRegister.this, "Registration failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }

    private void registerToDatabase(String doc_name, String doc_phone,
                                    String doc_email, String doc_specialization,
                                    String doc_gender, String doc_password,
                                    String doc_clinic_addr, String doc_clinic_phone){
        Log.d("registerToDatabase", "Db entry started");
        DatabaseReference docRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        Doctor doctor = new Doctor(doc_name, doc_phone, doc_email, doc_specialization, doc_gender, doc_clinic_addr, doc_clinic_phone);
        docRef.child(UID).setValue(doctor)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DocRegister.this, "Doctor registered successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(aVoid -> {
                    Toast.makeText(DocRegister.this, "Doctor registration unsuccessful", Toast.LENGTH_LONG).show();
                });
    }
}