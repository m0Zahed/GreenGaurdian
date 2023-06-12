//Help Page: Tips and advice for user on how to use the sensor readings to care for plants
package com.example.greenguardian390.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.greenguardian390.Models.Plant;
import com.example.greenguardian390.Models.UserProfile;
import com.example.greenguardian390.R;

public class helpPage extends AppCompatActivity {

    UserProfile currentuser;
    Plant selectedPlant;

    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        currentuser=(UserProfile) getIntent().getSerializableExtra("CurrentUser");
        selectedPlant = (Plant) getIntent().getSerializableExtra("plantClicked");

        back=findViewById(R.id.backButtonHelp);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(helpPage.this,PlantPage.class);
                intent.putExtra("CurrentUser",currentuser);
                intent.putExtra("plantClicked", selectedPlant);
                startActivity(intent);
            }
        });
    }
}