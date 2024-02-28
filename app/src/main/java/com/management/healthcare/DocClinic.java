package com.management.healthcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DocClinic extends Fragment {

    public interface OnDataPassedListener{
        void onDataPassed(String doc_clinic_addr, String doc_clinic_phone);
    }

    private OnDataPassedListener listener;

    private String doc_clinic_addr;
    private String doc_clinic_phone;
    private EditText docClinicAddr;
    private EditText docClinicPhone;

    public DocClinic() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_clinic, container, false);
        docClinicAddr = view.findViewById(R.id.docClinicAddr);
        docClinicPhone = view.findViewById(R.id.docClinicPhone);
        Button docRegister = view.findViewById(R.id.docRegConfirmBtn);
        Button prevButton = view.findViewById(R.id.docClinicPrevBtn);




        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DocInfo())
                        .commit();
            }
        });
        docRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_clinic_addr = docClinicAddr.getText().toString().trim();
                doc_clinic_phone = docClinicPhone.getText().toString().trim();
                if(validateAll(doc_clinic_addr, doc_clinic_phone)){
                    ((OnDataPassedListener) getActivity()).onDataPassed(doc_clinic_addr, doc_clinic_phone);
                    Toast.makeText(getActivity(), "DocClinic page data sent", Toast.LENGTH_SHORT).show();
                    Intent backToLogin = new Intent(getActivity(), LoginRegister.class);
                }else{
                    Toast.makeText(getActivity(), "Form is not working", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    private boolean validateAll(String doc_clinic_addr, String doc_clinic_phone){
        if(TextUtils.isEmpty(doc_clinic_addr)){
            Toast.makeText(getActivity(), "Please enter accurate clinic address", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(doc_clinic_phone) || doc_clinic_phone.length() != 10){
            Toast.makeText(getActivity(), "Please enter valid mobile number", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    public void sendDocClinicInfoToActivity(String doc_clinic_addr, String doc_clinic_phone){
        if(listener != null){
            listener.onDataPassed(doc_clinic_addr, doc_clinic_phone);
        }
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof OnDataPassedListener){
            listener = (OnDataPassedListener) context;
        }else{
            throw new ClassCastException(context.toString() + " must implement OnDataPassedListener");
        }
    }

}