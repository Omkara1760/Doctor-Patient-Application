package com.example.doctorpatientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class patient_login_for_doctor extends AppCompatActivity {
    public static String mobileno_of_user;
    EditText secretcode;
    Button submit;
    EditText mobileno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login_for_doctor);

        secretcode=findViewById(R.id.secretcode_for_doctor);
        mobileno=findViewById(R.id.mobilenofordoctor);
        submit=findViewById(R.id.submitpatientloginfo);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    {
                        mobileno_of_user=mobileno.getText().toString().trim();
                        DatabaseReference d= FirebaseDatabase.getInstance().getReference().child("+91"+mobileno_of_user).child("secretcode");
                        d.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue(String.class).equalsIgnoreCase(secretcode.getText().toString().trim()))
                                {
                                    startActivity(new Intent(patient_login_for_doctor.this,doct_choose_doc_for_p.class));
                                    finishAfterTransition();
                                }
                                else {
                                    Toast.makeText(patient_login_for_doctor.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DoctorHomePage.class));
        finishAfterTransition();
    }
}
