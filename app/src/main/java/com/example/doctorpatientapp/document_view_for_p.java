package com.example.doctorpatientapp;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



public class document_view_for_p extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference databaseReference;
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view_for_p);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.list);
        recyclerView.scrollToPosition(8);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("MyDocuments");
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

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

//    public void add__button_method(View view) {
//        startActivity(new Intent(this, my_draft_for_patient.class));
//        finishAfterTransition();
//    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public LinearLayout root;
//        public TextView description;

//        public ViewHolder(View itemView) {
//            super(itemView);
//            root = itemView.findViewById(R.id.list_root);
//            description = itemView.findViewById(R.id.description_recyclerview);
//        }

//        public void setdescription(String string) {
//            description.setText(string);
//        }
//}
    private void fetch() {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference(firebaseUser.getPhoneNumber()).child("MyDocuments");

        FirebaseRecyclerOptions<Grievience> options =
                new FirebaseRecyclerOptions.Builder<Grievience>()
                        .setQuery(query, new SnapshotParser<Grievience>() {
                            @NonNull
                            @Override
                            public Grievience parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Grievience(snapshot.child("description").getValue().toString(),
                                        snapshot.child("image").getValue().toString());

                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Grievience, ViewDraft.ViewHolder>(options) {
            @Override
            public ViewDraft.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview, parent, false);

                return new ViewDraft.ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ViewDraft.ViewHolder holder, final int position, final Grievience Grievience) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.setdescription(Grievience.getDescription());

                    }
                },10);

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(document_view_for_p.this, document_view_for_p_single.class);
                        i.putExtra("description",Grievience.getDescription());
                        i.putExtra("image",Grievience.getImage());
                        startActivity(i);
                        finishAfterTransition();
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,PatientHomePage.class));
        finishAfterTransition();
    }
}
