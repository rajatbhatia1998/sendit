package com.example.sendit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    Button regBtn;
    EditText email,username,pass,cPass;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        actionBar.setTitle("Register");

        regBtn = findViewById(R.id.button);
        email = findViewById(R.id.emailField);
        username = findViewById(R.id.nameField);
        pass = findViewById(R.id.editText3);
        cPass = findViewById(R.id.confirmPassword);
        progressDialog = new ProgressDialog(this);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText().toString();
                String user_txt = username.getText().toString();
                String pass_txt = pass.getText().toString();
                String cpass_txt = cPass.getText().toString();
                if(user_txt.isEmpty()){
                     username.setError("Enter Username");
                }
                else if (email_txt.isEmpty()){
                    email.setError("Enter Email");
                }
                else if(pass_txt.isEmpty()){
                    pass.setError("Enter Password");

                }
                else if( user_txt.length()>8){
                    username.setError("Should not more than 8 characters");

                }
                else if(cpass_txt.isEmpty()){
                    cPass.setError("Enter Confirm Password");
                }
                else if(!cpass_txt.equals(pass_txt)){
                    Toast.makeText(getApplicationContext(),"Password must be same",Toast.LENGTH_SHORT).show();
                }

                    else if (pass_txt.length()<6) {
                        Toast.makeText(getApplicationContext(),"Password Length Should be greater than 6",Toast.LENGTH_SHORT).show();
                    }
                else{
                    register(user_txt,email_txt,pass_txt);
                }

            }
        });
    }


    private void register(final String username, final String email, String pass){
        progressDialog.setMessage("Adding User");
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userID = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    HashMap<String,String> newUser= new HashMap<String,String>();

                    newUser.put("id",userID);
                    newUser.put("username",username);
                    newUser.put("email",email);
                    newUser.put("imageURL","default");
                    newUser.put("status","offline");
                    newUser.put("search",username.toLowerCase());

                    reference.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.cancel();
                                showToast("User Added ! Please Login");
                                Intent intent  = new Intent(registerActivity.this,loginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else{
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void showToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

}
