package com.example.doctorpatientapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Doctor_reg_page extends AppCompatActivity {
    private Uri uri;
    private ImageView doctor_profile_image;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_reg_page);
        Button doct_reg_sbmt = findViewById(R.id.submit_doctor_reg_pag);
        doct_reg_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Doctor_name, Dob, licence_number, Clinic_name, Taluka, Disrict, State, Email;
                Doctor_name = findViewById(R.id.Docotor_name);
                Disrict = findViewById(R.id.Doctor_Dist);
                Dob = findViewById(R.id.Doctor_dob);
                Email = findViewById(R.id.Doctor_email);
                Taluka = findViewById(R.id.Doctor_Taluka);
                State = findViewById(R.id.Doctor_state);
                licence_number = findViewById(R.id.Doctor_licence_number);
                Clinic_name = findViewById(R.id.Doctor_clinic_name);
                if (Doctor_name.getText().toString().trim().isEmpty() ||
                        Disrict.getText().toString().trim().isEmpty() ||
                        Dob.getText().toString().trim().isEmpty() ||
                        Taluka.getText().toString().trim().isEmpty() ||
                        licence_number.getText().toString().trim().isEmpty()||
                        Clinic_name.getText().toString().trim().isEmpty()||
                        State.getText().toString().trim().isEmpty() ||
                        Email.getText().toString().trim().isEmpty() ||
                        uri == null)
                {
                    Toast.makeText(Doctor_reg_page.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                }
                else {

                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert firebaseUser != null;
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("name");
                    databaseReference.setValue(Doctor_name.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("district");
                    databaseReference.setValue(Disrict.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("state");
                    databaseReference.setValue(State.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("taluka");
                    databaseReference.setValue(Taluka.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("clinic_name");
                    databaseReference.setValue(Clinic_name.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("licence_no");
                    databaseReference.setValue(licence_number.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("email");
                    databaseReference.setValue(Email.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("dob");
                    databaseReference.setValue(Dob.getText().toString());

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getPhoneNumber()).child("profile.jpg");
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseUser f = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference d = FirebaseDatabase.getInstance().getReference(f.getPhoneNumber()).child("Profile").child("isprofilecompleted");
                            d.setValue("true");
                            startActivity(new Intent(Doctor_reg_page.this, DoctorHomePage.class));
                            Toast.makeText(Doctor_reg_page.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                            finishAfterTransition();
                        }
                    });

                }
            }
        });

        doctor_profile_image = findViewById(R.id.doctor_profile_image);
        doctor_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeimagefromgallary();
            }
        });

    }

    public void takeimagefromgallary() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            uri = data.getData();
            doctor_profile_image.setImageURI(uri);
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,register.class));
        finishAfterTransition();
    }
}
