package com.cafedroid.gpstrackerapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,isSharing,code;
    Uri imageUri;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 = findViewById(R.id.textView);
        Intent myIntent = getIntent();
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


}
