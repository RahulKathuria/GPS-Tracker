package com.cafedroid.gpstrackerapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e4_email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e4_email = findViewById(R.id.editText3);
        auth = FirebaseAuth.getInstance();
    }

    public void goToPasswordActivity(View v){
        auth.fetchProvidersForEmail(e4_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if(task.isSuccessful()){
                    boolean check = !task.getResult().getProviders().isEmpty();

                    if(!check){
                        // Email not registered, we have to make a new user with this email
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email Already in use",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}
