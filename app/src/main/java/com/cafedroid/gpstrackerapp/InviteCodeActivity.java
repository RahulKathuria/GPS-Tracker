package com.cafedroid.gpstrackerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,isSharing,code;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog dialog;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        Intent myIntent = getIntent();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        if(myIntent!=null){
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            name = myIntent.getStringExtra("name");
            isSharing = myIntent.getStringExtra("isSharing");
            code = myIntent.getStringExtra("code");
            imageUri = myIntent.getParcelableExtra("imageUri");

        }
        t1.setText(code);


    }
    public void registerUser(View v){
        dialog.setMessage("Wait while your account is being created");
        dialog.show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    CreateUser createuser = new CreateUser(name,email,password,code,"false","NA","NA","NA");
                    user = auth.getCurrentUser();
                    userId = user.getUid();
                    reference.child(userId).setValue(createuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();

                            Toast.makeText(getApplicationContext(),"User registered successfully",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent myIntent = new Intent(InviteCodeActivity.this,MyNavigationDrawer.class);
                            startActivity(myIntent);

                        }
                        else{
                                dialog.dismiss();
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //Toast.makeText(getApplicationContext(),"Could not insert value in database",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }

            }
        });
    }

}
