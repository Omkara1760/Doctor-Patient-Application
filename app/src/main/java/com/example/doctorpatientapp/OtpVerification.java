package com.example.doctorpatientapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    Button verify_button;
    EditText otp;
    String phone_number;
    String verification_id;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        verify_button=findViewById(R.id.verify_button);
        otp=findViewById(R.id.otp_number);
        phone_number="+91"+getIntent().getStringExtra("phone_no");
        send_code(phone_number);

        firebaseAuth=FirebaseAuth.getInstance();


        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code=otp.getText().toString();
                if(code.trim().length()<6)
                {
                    otp.setError("Enter code");
                    otp.requestFocus();
                }
                else
                {
                    verify_code(code);
                }
            }
        });

    }
    private void send_code(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }
    private void verify_code(String code)
    {
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(verification_id,code);
        signInWithCredentials(phoneAuthCredential);
    }

    private void signInWithCredentials(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference d=FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("isprofilecompleted");
                    d.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(String.class)==null)
                            {
                                startActivity(new Intent(OtpVerification.this,register.class));
                                finishAfterTransition();
                            }else if(dataSnapshot.getValue(String.class).equals("true"))
                            {
                                FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference d=FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("post");
                                d.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue(String.class).equals("patient"))
                                        {
                                            startActivity(new Intent(OtpVerification.this,PatientHomePage.class));
                                            finishAfterTransition();
                                        }
                                        else
                                        {
                                            startActivity(new Intent(OtpVerification.this,DoctorHomePage.class));
                                            finishAfterTransition();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(OtpVerification.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verification_id=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                verify_code(code);
                otp.setText(code);
            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpVerification.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            //verify_button.setVisibility(View.VISIBLE);
        }
    };
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finishAfterTransition();
    }
}