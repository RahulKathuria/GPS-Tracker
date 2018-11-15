package com.cafedroid.gpstrackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    String email ;
    EditText e3_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        e3_password = findViewById(R.id.editText4);
        Intent passwordIntent = getIntent();
        if(passwordIntent!=null){
            email = passwordIntent.getStringExtra("email");

        }
    }

    public void goToNameActivity(View v){
                    if(e3_password.getText().toString().length()>6) {
                        Intent passwordIntent = new Intent(PasswordActivity.this, NameActivity.class);
                        passwordIntent.putExtra("email",email);
                        passwordIntent.putExtra("password",e3_password.getText().toString());
                        startActivity(passwordIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password length should be greater than 6 characters",Toast.LENGTH_LONG).show();
                    }
    }

}
