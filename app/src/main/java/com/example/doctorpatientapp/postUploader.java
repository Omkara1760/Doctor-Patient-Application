package com.example.doctorpatientapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


@SuppressWarnings("ALL")
public class postUploader extends AppCompatActivity {
    Uri imageuri;
    ImageView imageView;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private static final int REQUEST_CALL_GALLARY = 1;
    int counter = 0, c = 0, c1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postuploader);
    }

    public void button_gallary(View v) {
        openfilechooser();
    }


    public void openfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CALL_GALLARY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CALL_GALLARY && resultCode == RESULT_OK && data != null) {
            imageView = findViewById(R.id.imageView_camera);
            imageView.setImageURI(data.getData());
            imageuri = data.getData();
        }
    }

    public void button_upload(View v) {
        EditText discription = findViewById(R.id.description);
        if (discription.getText().toString().trim().length() > 0) {
            DatabaseReference currentdatabaseref = FirebaseDatabase.getInstance().getReference().child("currentcounter");
            currentdatabaseref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (c == 0) {
                        counter = Integer.parseInt(dataSnapshot.getValue(String.class));
                        upload();
                        c = 1;
                    } else {
                        return;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(this, "Please fill the details first", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,DoctorHomePage.class));
        finishAfterTransition();
    }

    public void upload() {

        storageReference = FirebaseStorage.getInstance().getReference("Posts");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Uploading.." + " 0%");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (imageuri != null) {
            final String time = String.valueOf(System.currentTimeMillis());
            StorageReference fileref = storageReference.child(String.valueOf(counter - 1));
            fileref.putFile(imageuri).addOnProgressListener(this, new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMessage("Uploading    " + (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()) + " % completed...");
                }
            })
                    .addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            EditText discription = postUploader.this.findViewById(R.id.description);
                            databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                            DatabaseReference newdatabasereference = databaseReference.child(String.valueOf(counter - 1));
                            newdatabasereference.child("description").setValue(discription.getText().toString().trim());
                            newdatabasereference.child("image").setValue(String.valueOf(counter - 1));

                            progressDialog.setMessage("Uploading  100 % completed...");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    DatabaseReference currentdatabaseref = FirebaseDatabase.getInstance().getReference().child("currentcounter");
                                    currentdatabaseref.setValue(String.valueOf(counter - 1));

                                    progressDialog.dismiss();
                                    Toast.makeText(postUploader.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }, 2000);
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

}
