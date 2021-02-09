package com.example.doctorpatientapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

public class my_draft_for_patient extends AppCompatActivity {
    ProgressDialog progressDialog;
    Uri imageuri;
    FirebaseUser firebaseUser;
    ImageView imageView;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_draft_for_patient);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    }


    public void button_camera(View v) {
        openfilechooser();
    }

    public void openfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CALL && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            imageView = findViewById(R.id.imageView_camera);
            imageView.setImageURI(imageuri);
        }
    }

    public void button_upload(View v) {
        EditText discription = findViewById(R.id.description);
        if (discription.getText().toString().trim().length() > 0) {
            upload();
        } else {
            Toast.makeText(this, "Please fill the details first", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ViewDraft.class));
        finishAfterTransition();
    }

    public void upload() {
        storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getPhoneNumber()).child("MyDraft");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (imageuri != null) {
            final String time = String.valueOf(System.currentTimeMillis());
            StorageReference fileref = storageReference.child(time);
            fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    EditText discription = findViewById(R.id.description);
                    databaseReference = FirebaseDatabase.getInstance().getReference
                            (firebaseUser.getPhoneNumber()).child("MyDraft");
                    DatabaseReference newdatabasereference = databaseReference.push();
                    newdatabasereference.child("description").setValue(discription.getText().toString().trim());
                    newdatabasereference.child("image").setValue(time);
                    DatabaseReference d = FirebaseDatabase.getInstance().getReference("/count");
                    d.setValue(String.valueOf(System.currentTimeMillis()));
                    progressDialog.dismiss();
                    Toast.makeText(my_draft_for_patient.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(my_draft_for_patient.this, ViewDraft.class));
                    finishAfterTransition();

                }
            });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

}
