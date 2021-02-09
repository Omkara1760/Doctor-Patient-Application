package com.example.doctorpatientapp;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class DoctorHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView prof_p;
    NavigationView navigationView;
    TextView name_of_doctor;
    public DrawerLayout drawerLayout_admin;
    File storagedirectory, image_;

    //

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;

    int c = 0;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startactivity_doctor);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        drawerLayout_admin = findViewById(R.id.drawer_layout_admin);
        Toolbar toolbar_admin = findViewById(R.id.tool_bar_Doctor);
        setSupportActionBar(toolbar_admin);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout_admin, toolbar_admin, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout_admin.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);


        View v = navigationView.getHeaderView(0);
        prof_p = v.findViewById(R.id.profile_icon_doctor);
        name_of_doctor = v.findViewById(R.id.doctor_username);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("Profile").child("name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name_of_doctor.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        storagedirectory = getDataDir();
        try {
            image_ = File.createTempFile("image", ".jpg", storagedirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(firebaseUser.getPhoneNumber()).child("profile.jpg");
        storageReference.getFile(image_).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                prof_p.setImageURI(Uri.fromFile(image_));
                image_.delete();
            }
        });

        //
            recyclerView = findViewById(R.id.posts_d);
            recyclerView.scrollToPosition(8);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            fetch();

            //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
            databaseReference.keepSynced(true);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    adapter.startListening();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    adapter.startListening();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    public class ViewHolder extends RecyclerView.ViewHolder {
        File image_;
        File storagedirectory;
        LinearLayout root;
        TextView description;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            description = itemView.findViewById(R.id.description_recyclerview);
            image = itemView.findViewById(R.id.image_recyclerview);
        }

        public void setdescription(String string) {
            description.setText("Description:"+string);
        }


        public void setImage(String string) {
            storagedirectory = getDataDir();
            File EdiProject = new File(storagedirectory + "/EdiProject");
            EdiProject.mkdir();
            File PostImages = new File(EdiProject + "/PostImages");
            PostImages.mkdir();
            image_ = new File(PostImages + "/" + string + ".jpg");
            if (!image_.exists()) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Posts").child(string);
                storageReference.getFile(image_).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        image.setImageURI(Uri.fromFile(image_));
                    }
                });
            } else {
                image.setImageURI(Uri.fromFile(image_));
            }
        }
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Posts");
        query.orderByPriority();

        FirebaseRecyclerOptions<post_grivience> options =
                new FirebaseRecyclerOptions.Builder<post_grivience>()
                        .setQuery(query, new SnapshotParser<post_grivience>() {
                            @NonNull
                            @Override
                            public post_grivience parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new post_grivience(snapshot.child("description").getValue().toString(),
                                        snapshot.child("image").getValue().toString()
                                );
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<post_grivience, DoctorHomePage.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position, @NonNull final post_grivience post_grivience) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.setdescription(post_grivience.getDescription());
                        viewHolder.setImage(post_grivience.getImage());

                    }
                }, 10);


            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview, parent, false);

                return new DoctorHomePage.ViewHolder(view);
            }



        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

   //




    @Override
    public void onBackPressed() {
        if (drawerLayout_admin.isDrawerOpen(GravityCompat.START)) {
            drawerLayout_admin.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
            finishAfterTransition();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {


           case R.id.doc_profile:

                startActivity(new Intent(DoctorHomePage.this,Doctor_profile_view.class));
                finishAfterTransition();
                break;

            case R.id.login_for_pat:

                startActivity(new Intent(DoctorHomePage.this,patient_login_for_doctor.class));
                finishAfterTransition();
                break;
            case R.id.document_store:

                startActivity(new Intent(DoctorHomePage.this,ViewDraft.class));
                finishAfterTransition();
                break;
            case R.id.upload_post_for_p:

                startActivity(new Intent(DoctorHomePage.this,postUploader.class));
                finishAfterTransition();
                break;
            case R.id.nav_logout_doctor:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(DoctorHomePage.this,MainActivity.class));
                finishAfterTransition();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
        }
        drawerLayout_admin.closeDrawer(GravityCompat.START);
        return true;
    }

}
