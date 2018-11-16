package com.cafedroid.gpstrackerapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1 , e2;

    PermissionManager manager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();


        if(auth.getCurrentUser()==null){
            setContentView(R.layout.activity_login);
            manager = new PermissionManager() {
            };
            manager.checkAndRequestPermissions(this);
            Log.e("TAG", "onCreate: auth is null" );
        }
        else{
            Log.e("TAG", "onCreate: started drawer" );
            Intent myIntent = new Intent(LoginActivity.this,MyNavigationDrawer.class);
            startActivity(myIntent);
            finish();
        }
        e1 = findViewById(R.id.editText);
        e2 = findViewById(R.id.editText2);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        manager.checkResult(requestCode, permissions, grantResults);
        ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;
        if(denied_permissions.isEmpty()){
            Toast.makeText(getApplicationContext(),"Permission Enabled",Toast.LENGTH_SHORT).show();
        }

    }

    public void login(View v){
        auth.signInWithEmailAndPassword(e1.getText().toString(),e2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(LoginActivity.this,MyNavigationDrawer.class);
                    startActivity(myIntent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong User Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void goToRegister(View v){

        Intent myIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(myIntent);
    }
}
