
package com.example.doctorpatientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Doctor_profile_view extends AppCompatActivity {
    private File image_;
    private int c=0;
    private PopupWindow popupWindow;
    ImageView imageView;
    File localFile;
    String value;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextView name,clinicname,licencenumb,dob,taluka,district,state,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_view);

        name=findViewById(R.id.Name_d);
        clinicname=findViewById(R.id.clinic_name_d);
        licencenumb=findViewById(R.id.licence_numb_d);
        dob=findViewById(R.id.dob_d);

        taluka=findViewById(R.id.taluka_d);

        district=findViewById(R.id.district_d);
        state=findViewById(R.id.state_d);
        email=findViewById(R.id.email_d);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference().child(firebaseUser.getPhoneNumber()).child("profile.jpg");
        imageView=findViewById(R.id.imageViewd);
        File storagedirectory=getDataDir();
        try {
            image_= File.createTempFile("profile",".jpg",storagedirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        storageReference.getFile(image_)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        details();
                        imageView.setImageURI(Uri.fromFile(image_));
                        image_.delete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }


    public void details()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText("  Name: "+dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/clinic_name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clinicname.setText("  Clinic Name: "+dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/licence_no");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                licencenumb.setText("  Licence Number: "+dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/taluka");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taluka.setText("  Taluka: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/district");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                district.setText("  District: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/state");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                state.setText("  State: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/dob");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dob.setText("  Birthdate: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()+"/Profile"+"/email");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email.setText("  Email: "+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,DoctorHomePage.class));
        finishAfterTransition();
    }

}
