package com.management.healthcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class DocInfo extends Fragment {

    public interface OnDataPassedListener{
        void onDataPassed(String doc_name, String doc_phone, String doc_email, String doc_specialization, String doc_gender, String password);
    }

    private OnDataPassedListener listener;
    private String doc_name;
    private String doc_phone;
    private String doc_email;
    private String doc_specialization; //Spinner value
    private String doc_gender; //RadioButton value
    private String doc_password;
    private String doc_confirm_password;
    private EditText docName;
    private EditText docPhone;
    private EditText docEmail;
    private EditText password;
    private EditText confirmPassword;
    private RadioGroup radioBtnGroup;
    private RadioButton selectedChoice;

    public DocInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc_info, container, false);
        docName = view.findViewById(R.id.DocName);
        docPhone = view.findViewById(R.id.DocPhone);
        docEmail = view.findViewById(R.id.DocEmail);
        radioBtnGroup = view.findViewById(R.id.radioBtnGroup);
        password = view.findViewById(R.id.docPassword);
        confirmPassword = view.findViewById(R.id.docConfirmPassword);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Spinner specialSpinner = (Spinner) view.findViewById(R.id.docSpecial);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.doc_specialization,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialSpinner.setAdapter(adapter);

        Button nextButton = view.findViewById(R.id.docInfoNextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc_name = docName.getText().toString().trim();
                doc_phone = docPhone.getText().toString().trim();
                doc_email = docEmail.getText().toString().trim();
                doc_specialization = specialSpinner.getSelectedItem().toString().trim();
                int radioSelectedId = radioBtnGroup.getCheckedRadioButtonId();
                if (radioSelectedId != -1){
                    selectedChoice = view.findViewById(radioSelectedId);
                    doc_gender = selectedChoice.getText().toString().trim();
                }else{
                    doc_gender = "";
                }
                doc_password = password.getText().toString().trim();
                doc_confirm_password = confirmPassword.getText().toString().trim();


                if(validateAllFields(doc_name, doc_phone, doc_email, doc_specialization, doc_gender, doc_password, doc_confirm_password)){
                    ((OnDataPassedListener) getActivity()).onDataPassed(doc_name, doc_phone, doc_email, doc_specialization, doc_gender, doc_password);
                    Toast.makeText(getActivity(), "DocInfo page data sent", Toast.LENGTH_LONG).show();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new DocClinic(), "doc_clinic_fragment")
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        return view;
    }
    private boolean validateAllFields(String doc_name, String doc_phone, String doc_email, String doc_specialization, String doc_gender, String doc_password, String doc_confirm_password){
        if(TextUtils.isEmpty(doc_name)){
            Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(doc_phone) || doc_phone.length() != 10){
            Toast.makeText(getActivity(), "Please enter valid mobile number", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(doc_email)){
            Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(doc_gender)){
            Toast.makeText(getActivity(), "Please select your gender", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.equals(doc_specialization, "Select Specialization")){
            Toast.makeText(getActivity(), "Please select your specialization", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(doc_password)){
            Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_LONG).show();
            return false;
        }
        if(TextUtils.isEmpty(doc_confirm_password) || doc_confirm_password.length() < 8){
            Toast.makeText(getActivity(), "Password must be 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(TextUtils.equals(doc_password,doc_confirm_password))){
            Toast.makeText(getActivity(), "Password fields don't match!", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    public void sendDocInfoToActivity(String doc_name, String doc_phone, String doc_email, String doc_specialization, String doc_gender, String password){
        if(listener != null){
            listener.onDataPassed(doc_name,doc_phone,doc_email,doc_specialization,doc_gender,password);
        }
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof OnDataPassedListener){
            listener = (OnDataPassedListener) context;
        }else{
            throw new ClassCastException(context.toString()
            + " must implement OnDataPassedListener interface");
        }
    }


}