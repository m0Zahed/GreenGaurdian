//Login Page: Page that lets the user log into their account
package com.example.greenguardian390.Views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenguardian390.Models.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.os.Bundle;

import com.example.greenguardian390.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    protected EditText mUsername,mPassword;
    Button HitLogin;
    private TextView forgotPassword;



    DatabaseReference mDatabase;
    ProgressBar progressBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUsername=findViewById(R.id.UserName);
        mPassword=findViewById(R.id.PassWord);

        //when user clicks on login button
        HitLogin=findViewById(R.id.Login1);
        HitLogin.setOnClickListener(this);

        progressBar=findViewById(R.id.progressBarLogin);
        //when user clicks on forgot password
        forgotPassword=findViewById(R.id.ForgotPassword);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Login1:
                userLogin(); //when user clicks on login button, execute userLogin function
                break;
        }

        switch (view.getId()){
            case R.id.ForgotPassword:
                startActivity(new Intent(this,ForgetPassword.class));
                break;
        }
    }

    private void userLogin() {
        String usernameInputted=mUsername.getText().toString().trim();
        String password=mPassword.getText().toString().trim();

        //make sure user enters all info correctly
        if (usernameInputted.isEmpty()){
            mUsername.setError("username is mandatory");
            mUsername.requestFocus();
            return;
        }
        if(password.isEmpty()){
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }
        if(password.length()< 5){
            mPassword.setError("The minimum requirement is 5 character");
            mPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //get the current profile object from database
        mDatabase= FirebaseDatabase.getInstance().getReference("userProfile/"+usernameInputted);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if user exists in database, let them log in and take them to main page,
                // and send the current profile to the main page
                //and start the notifications service
                UserProfile userProfile= snapshot.getValue(UserProfile.class);

                if(userProfile!=null) {

                    if(userProfile.getPassword().equals(password))
                    {
                        Intent intent = new Intent(LoginActivity.this, MainPage.class);

                        intent.putExtra("currentProfile", userProfile);

                        startActivity(intent);

                        Intent intentNotification = new Intent(LoginActivity.this,sensorChangeNotifications.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("currentProfile", userProfile);
                        intentNotification.putExtras(bundle);
                        startService(intentNotification);
                    }
                    /*else {
                        Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
                    }*/

                }else {
                    Toast.makeText(LoginActivity.this,"Please check username spelling or create account in register page",Toast.LENGTH_LONG).show();
                }


                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(LoginActivity.this,"Please check username spelling or create account in register page",Toast.LENGTH_LONG).show();
            }

        });


    }
}

