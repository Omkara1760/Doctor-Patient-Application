package com.example.doctorpatientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class document_view_for_p_single extends AppCompatActivity {
    private String image;
    private ImageView imageView;
    private File image_,storagedirectory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view_for_p_single);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        imageView=findViewById(R.id.post_details_imageview);

        image=getIntent().getStringExtra("image");

        storagedirectory=getDataDir();

        try {
            image_= File.createTempFile("image",".jpg",storagedirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(firebaseUser.getPhoneNumber()).child("MyDocuments").child(image);
        storageReference.getFile(image_).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                imageView.setImageURI(Uri.fromFile(image_));
                image_.delete();


            }
        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,document_view_for_p.class));
        finishAfterTransition();
    }
}
