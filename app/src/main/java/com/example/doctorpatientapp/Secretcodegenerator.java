package com.example.doctorpatientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Secretcodegenerator extends AppCompatActivity {
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretcodegenerator);



        final TextView secretcode=findViewById(R.id.secretcode_view);
        time =String.valueOf(System.currentTimeMillis());
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("secretcode");
        databaseReference.setValue(time).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                secretcode.setText(time);
            }
        });



    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,PatientHomePage.class));
        finishAfterTransition();
    }


}
