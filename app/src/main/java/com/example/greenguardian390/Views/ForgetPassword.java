//ForgetPassword Page: Lets user change password to their account
package com.example.greenguardian390.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.util.Patterns;
import android.widget.Toast;

import com.example.greenguardian390.Models.UserProfile;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.greenguardian390.R;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private EditText userName, email, newPassword;
    private Button hitReset;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName=findViewById(R.id.UserNamePassword);
        email=findViewById(R.id.EmailPassword);
        newPassword=findViewById(R.id.newPassword);
        hitReset=findViewById(R.id.ResetButton);
        progressBar=findViewById(R.id.progressBarPassword);

        hitReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ResetButton:
                hitReset();
                break;
        }
    }

    private void hitReset()
    {
        String usernameInputted=userName.getText().toString().trim();
        String emailInputted=email.getText().toString().trim();
        String passwordInputted=newPassword.getText().toString().trim();

        if(usernameInputted.isEmpty()){
            userName.setError("Username is Required!");
            userName.requestFocus();
            return;
        }
        if(emailInputted.isEmpty()){
            email.setError("Email is Required!");
            email.requestFocus();
            return;
        }
        if(passwordInputted.isEmpty()){
            newPassword.setError("Password is Required!");
            newPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailInputted).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        if(passwordInputted.length()< 5){
            newPassword.setError("The password requirement is above 5 characters");
            newPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mDatabase= FirebaseDatabase.getInstance().getReference("userProfile/"+usernameInputted);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserProfile userProfile= snapshot.getValue(UserProfile.class);

                if(userProfile!=null) {

                    if(userProfile.getEmail().equals(emailInputted))
                    {
                        mDatabase=FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("userProfile").child(usernameInputted).child("password").setValue(passwordInputted).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgetPassword.this,"Password has been changed successfully, please login in login page",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                    startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                                }
                                else {
                                    Toast.makeText(ForgetPassword.this,"Password change failed! Please try again.",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                    }
                }
                else
                {
                    Toast.makeText(ForgetPassword.this,"Account does not exist, please go to registration page",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}