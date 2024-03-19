package com.management.healthcare;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{
    private final List<Appointment> appointments;
    private boolean isDoc;
    public AppointmentAdapter(List<Appointment> appointments, boolean isDoc){
        this.appointments = appointments;
        this.isDoc = isDoc;
    }


    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_layout, parent, false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        if(isDoc){
            holder.nameTextView.setText(appointment.getUserName());
            holder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callDoctor = new Intent(Intent.ACTION_DIAL);
                    callDoctor.setData(Uri.parse("tel:" + appointment.getUserPhone()));
                    holder.itemView.getContext().startActivity(callDoctor);
                }
            });
        }else{
            holder.nameTextView.setText(appointment.getDoctorName());
            holder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callDoctor = new Intent(Intent.ACTION_DIAL);
                    callDoctor.setData(Uri.parse("tel:" + appointment.getDoctorClinicPhone()));
                    holder.itemView.getContext().startActivity(callDoctor);
                }
            });
        }
        holder.cancelAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference apptRef = FirebaseDatabase.getInstance().getReference("Appointments").child(appointment.getApptID()+"");
                apptRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(holder.itemView.getContext(), "Record deleted!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.itemView.getContext(), "Record deletion unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        holder.doctorAddressTextView.setText(appointment.getVenue());
        holder.doctorSpecialTextView.setText(appointment.getDoctorSpecialization());
        Calendar calendar = Calendar.getInstance();
        Date apptDate = new Date(appointment.getDateTime());
                    calendar.setTime(apptDate);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    String monthStr = "MON";
                    if(month == 1)
                        monthStr = "JAN";
                    if(month == 2)
                        monthStr = "FEB";
                    if(month == 3)
                        monthStr = "MAR";
                    if(month == 4)
                        monthStr = "APR";
                    if(month == 5)
                        monthStr = "MAY";
                    if(month == 6)
                        monthStr = "JUN";
                    if(month == 7)
                        monthStr = "JUL";
                    if(month == 8)
                        monthStr = "AUG";
                    if(month == 9)
                        monthStr = "SEP";
                    if(month == 10)
                        monthStr = "OCT";
                    if(month == 11)
                        monthStr = "NOV";
                    if(month == 12)
                        monthStr = "DEC";
                    String date = dayOfMonth + " " + monthStr;
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    String hourStr = String.format("%02d", hour);
                    int minute = calendar.get(Calendar.MINUTE);
                    String minuteStr = String.format("%02d", minute);
                    String time = hourStr + ":" + minuteStr;
                    String dateTime = date + " " + time;
                    holder.dateTimeTextView.setText(dateTime);


    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dateTimeTextView;
        TextView doctorAddressTextView;
        TextView doctorSpecialTextView;
        Button callBtn;
        TextView cancelAppt;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            cancelAppt = itemView.findViewById(R.id.cancelAppt);
            callBtn = itemView.findViewById(R.id.callBtn);
            nameTextView = itemView.findViewById(R.id.name);
            dateTimeTextView = itemView.findViewById(R.id.apptTime);
            doctorSpecialTextView = itemView.findViewById(R.id.docSpecialization);
            doctorAddressTextView = itemView.findViewById(R.id.docAddress);
        }
    }
}
