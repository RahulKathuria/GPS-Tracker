package com.cafedroid.gpstrackerapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity {

    String email,password;
    EditText e5;
    Button b6;
    CircleImageView circleImageView;
    Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e5 = findViewById(R.id.editText5);
        b6 = findViewById(R.id.button6);
        circleImageView = findViewById(R.id.circleImageView);

        Intent myIntent = getIntent();
        if(myIntent!=null){
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
        }
    }


    public void generateCode(View v){

        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String date = format1.format(myDate);
        Random r = new Random();
        int n = 100000 +  r.nextInt(900000);
        String code = String.valueOf(n);

        if(resultUri!=null){}
        else{
            Toast.makeText(getApplicationContext(),"Please Choose an Image",Toast.LENGTH_SHORT).show();
        }

    }
}
