package com.cafedroid.gpstrackerapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCircleActivity extends AppCompatActivity {


    ListView memberListView;
    ArrayAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<String> namelist;
    DatabaseReference reference, userReference;
    String circleMemberId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
//
        memberListView = findViewById(R.id.memberLV);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        namelist = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);

        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("circleMembers");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namelist.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dss : dataSnapshot.getChildren()){
                        circleMemberId = dss.getValue(String.class);
                        namelist.add(circleMemberId);
                        adapter.notifyDataSetChanged();
//                        userReference.child(circleMemberId)
//                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        createUser = dataSnapshot.getValue(CreateUser.class);
//                                        namelist.add(createUser);
//                                        adapter.notifyDataSetChanged();
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        Toast.makeText(MyCircleActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MyCircleActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });






        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,namelist);
        memberListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
