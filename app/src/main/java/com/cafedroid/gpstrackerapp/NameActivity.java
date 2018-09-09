package com.cafedroid.gpstrackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class NameActivity extends AppCompatActivity {

    String email,password;
    EditText e5;
    Button b6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e5 = findViewById(R.id.editText5);
        b6 = findViewById(R.id.button6);

        Intent myIntent = getIntent();
        if(myIntent!=null){
            email = myIntent.getStringExtra("email");

        }
    }
}
