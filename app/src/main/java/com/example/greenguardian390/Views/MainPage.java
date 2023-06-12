//Main Page: Page that shows the user their plants
//Each plant lets the user go to that plant's page
//Also lets user either logout or go to add a plant page
package com.example.greenguardian390.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.greenguardian390.Models.Plant;
import com.example.greenguardian390.Models.UserProfile;
import com.example.greenguardian390.R;
import com.example.greenguardian390.Views.AddPlantPage;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    static final int REQUEST_CODE = 1;
    private NotificationManager notificationManager;
    private int notificationId;
    private Button button, logout;

    private UserProfile currentuser;

    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);



        listView=findViewById(R.id.userPlants);

        //get the current profile logged in
        currentuser=(UserProfile) getIntent().getSerializableExtra("currentProfile");

        //get the user's plants and display them onto the page
        ArrayList<Plant> currentUserPlants=currentuser.getUserPlants();

        ArrayList<String> plantNames=new ArrayList<>();

        if(currentUserPlants.size()>0)
        {
            for(Plant userPlant : currentUserPlants)
            {
                String plantName;
                plantNames.add(userPlant.getPlantName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, plantNames);
            listView.setAdapter(adapter);
        }

        logout=findViewById(R.id.logoutButton);
        //let the user logout and stop the notifications service once they log out
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainPage.this,MainActivity.class);
                startActivity(intent);
                stopService(new Intent(MainPage.this, sensorChangeNotifications.class));
                finish();
            }
        });

        //when add a plant button is clicked, execute openAddPlantPage function
        button = (Button) findViewById(R.id.AddPlantButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddPlantPage();


            }
        });

        //when a plant in the list is clicked, first we search in the arraylist which plant was clicked
        //then the user goes to the plant page and sends the current profile and the plant selected.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlantName = (String) adapterView.getItemAtPosition(i);
                Plant selectedPlant=new Plant();
                for (Plant userPlant : currentUserPlants)
                {
                    if(userPlant.getPlantName().equals(selectedPlantName))
                    {
                        selectedPlant=userPlant;
                        break;
                    }
                }
                Intent intent=new Intent(MainPage.this,PlantPage.class);
                intent.putExtra("CurrentUser",currentuser);
                intent.putExtra("plantClicked", selectedPlant);
                startActivity(intent);

            }
        });
    }


    //when user clicks on add a plant button, they are taken to add a plant page
    //current profile is sent to that page
    public void openAddPlantPage() {
        UserProfile currentuser=(UserProfile) getIntent().getSerializableExtra("currentProfile");
        Intent intent = new Intent(this, AddPlantPage.class);
        intent.putExtra("CurrentUser", currentuser);
        startActivity(intent);
    }


}