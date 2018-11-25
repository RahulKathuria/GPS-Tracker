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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InviteCodeActivity extends AppCompatActivity {

    String name,email,password,isSharing,code;
    Uri imageUri;
    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressDialog dialog;
    String userId;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        t1 = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference().child("user images");
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


                                //save the image to firebase storage
                                final StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                sr.putFile(imageUri)
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if(task.isSuccessful()){

                                                    String download_image_path = sr.getDownloadUrl().toString();
                                                    reference.child(user.getUid()).child("imageUrl").setValue(download_image_path)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        dialog.dismiss();

                                                                        sendVerificationEmail();
                                                                        Intent myIntent = new Intent(InviteCodeActivity.this,MyNavigationDrawer.class);
                                                                        startActivity(myIntent);
                                                                        finish();
                                                                    }
                                                                    else{
                                                                        dialog.dismiss();

                                                                        task.addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });



                            }
                        }

                    });
                }

            }
        });
    }






    public void sendVerificationEmail(){
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Email Sent for verification",Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Could Not send Verification Email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
