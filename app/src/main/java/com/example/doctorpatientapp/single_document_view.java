package com.example.doctorpatientapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class single_document_view extends AppCompatActivity {
    private String image;
    private ImageView imageView;
    private File image_,storagedirectory;


    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,ViewDraft.class));
        finishAfterTransition();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_document_view);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        imageView=findViewById(R.id.post_details_imageview);

        image=getIntent().getStringExtra("image");

        storagedirectory=getDataDir();

        try {
            image_= File.createTempFile("image",".jpg",storagedirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(firebaseUser.getPhoneNumber()).child("MyDraft").child(image);
        storageReference.getFile(image_).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                imageView.setImageURI(Uri.fromFile(image_));
                image_.delete();


            }
        });

    }



}
