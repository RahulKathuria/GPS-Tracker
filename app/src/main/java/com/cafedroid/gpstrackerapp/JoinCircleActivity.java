package com.cafedroid.gpstrackerapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {



    Pinview pinview;

    DatabaseReference reference,currentReference,circleReference;
    FirebaseUser user;
    FirebaseAuth auth;
    String current_user_id,join_user_id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        pinview = (Pinview)findViewById(R.id.pinview);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        current_user_id= user.getUid();
        name = user.getDisplayName();




    }


    public void submitButton(View v){

        Query query =reference.orderByChild("code").equalTo(pinview.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    CreateUser createUser;
                    for(DataSnapshot childss: dataSnapshot.getChildren()) {

                        createUser = childss.getValue(CreateUser.class);
                        join_user_id = childss.getKey();
                        circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(join_user_id).child("circleMembers");

                        CircleJoin circleJoin = new CircleJoin(current_user_id);
                        CircleJoin circleJoin1 = new CircleJoin(join_user_id);


                        circleReference.child(current_user_id).setValue(name)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "User joined Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }

                }
                else{
                    Toast.makeText(JoinCircleActivity.this, "Invalid Circle Code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}