//Registration Page: User registers for their account
package com.example.greenguardian390.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenguardian390.Models.UserProfile;
import com.example.greenguardian390.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;

    private TextView banner;
    private EditText userName,editEmail,editPassword;
    private Button HitRegister;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        HitRegister=findViewById(R.id.Register_button);
        userName=findViewById(R.id.UserName);
        editEmail=findViewById(R.id.Email1);
        editPassword=findViewById(R.id.Password1);
        progressBar=findViewById(R.id.progressBarRegister);

        HitRegister.setOnClickListener(this); //register button clicked, go to HitRegister function


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Register_button:
                HitRegister();
                break;
        }
    }


    private void HitRegister() {
        //User inputs are converted to string
        String user=userName.getText().toString().trim();
        String Email=editEmail.getText().toString().trim();
        String Password=editPassword.getText().toString().trim();

        //Error messages to not let user register without filling in proper requirements
        if(user.isEmpty()){
            userName.setError("Username is Required!");
            userName.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            editEmail.setError("Email is Required!");
            editEmail.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            editPassword.setError("Password is Required!");
            editPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editEmail.setError("Please provide a valid email");
            editEmail.requestFocus();
            return;
        }
        if(Password.length()< 5){
            editPassword.setError("The password requirement is above 5 character");
            editPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Check to see if account already exists in database
        mDatabase= FirebaseDatabase.getInstance().getReference("userProfile/"+user);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile= snapshot.getValue(UserProfile.class);

                //if the user does not exist in database
                //let them register
                if(userProfile==null)
                {
                    mDatabase=FirebaseDatabase.getInstance().getReference();
                    UserProfile profile =new UserProfile(user,Password,Email);

                    mDatabase.child("userProfile").child(user).setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"User has been register successfully, please login in login page",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.VISIBLE);
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,"Registration Failed! Please try again.",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    });
                }
                else if (userProfile != null)
                {
                    //If user already exists don't let them register
                   // Toast.makeText(RegisterActivity.this,"Registration Failed! Account already exists.",Toast.LENGTH_LONG).show();
                   // progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
