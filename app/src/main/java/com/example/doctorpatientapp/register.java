package com.example.doctorpatientapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private Button doctor_rg;
    private Button patient_rg;
    private Button back_to_main_act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        doctor_rg=findViewById(R.id.as_doctor);

        doctor_rg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(register.this, firebaseUser.getPhoneNumber(), Toast.LENGTH_LONG).show();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("post");
                databaseReference.setValue("Doctor");
                startActivity(new Intent(register.this, Doctor_reg_page.class));
                finishAfterTransition();
            }
        });

        patient_rg=findViewById(R.id.as_patient);
        patient_rg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("post");
                databaseReference.setValue("Patient");
                startActivity(new Intent(register.this,Patient_reg_page.class));
                finishAfterTransition();
            }
        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finishAfterTransition();
    }

}
