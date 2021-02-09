package com.example.doctorpatientapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    Button continue_button;
    String s;
    EditText number;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser f=FirebaseAuth.getInstance().getCurrentUser();
        if(f!=null)
        {
            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference d=FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("isprofilecompleted");
            d.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.getValue(String.class)==null)
                    {
                        startActivity(new Intent(MainActivity.this,register.class));
                        finishAfterTransition();
                    }
                    else if(dataSnapshot.getValue(String.class).equals("true"))
                    {
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference d=FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("post");
                        d.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot.getValue(String.class).equals("Patient"))
                                {
                                    startActivity(new Intent(MainActivity.this,PatientHomePage.class));
                                    finishAfterTransition();
                                }
                                else
                                {
                                    startActivity(new Intent(MainActivity.this,DoctorHomePage.class));
                                    finishAfterTransition();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        continue_button = findViewById(R.id.register_button);
        number = findViewById(R.id.Contact_numb);
        continue_button.setActivated(false);
        continue_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (number.getText().toString().trim().length() < 10 || number.getText().toString().trim().length() > 10)
                {
                    number.setError("Invalid number");
                    number.requestFocus();
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, OtpVerification.class);
                    intent.putExtra("phone_no", number.getText().toString().trim());
                    startActivity(intent);
                    finishAfterTransition();
                }
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
    }
}
