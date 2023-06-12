//Notifications Background Service: Implements the notifications shown in app after user logs in

package com.example.greenguardian390.Views;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.greenguardian390.Models.Plant;
import com.example.greenguardian390.Models.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.greenguardian390.R;

public class sensorChangeNotifications extends Service {

    private static final String CHANNEL_ID = "The Green Guardian";
    private NotificationManager mNotificationManager;

    private String notificationMessage;

    private UserProfile currentUser;

    DatabaseReference mDatabase;

    private int notificationId = 0;

    public sensorChangeNotifications() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //get sensor data from firebase and send notifications when sensor data is above plants' values thresholds
        mDatabase = FirebaseDatabase.getInstance().getReference("SenData");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot d :snapshot.getChildren())
                {
                    if (d.getKey().toLowerCase().contains("moisture"))
                    {
                        long currentTime = System.currentTimeMillis();
                        Number value = (Number)d.getValue();
                        //check if the user is logged in and has plants
                        if(currentUser != null && currentUser.getUserPlants().size()>0)
                        {
                            //for each plant if the plant's current soil moisture (sensor data)
                            // is higher by 40 + plant type's soil moisture or lower by 40 - plant type's soil moisture than plant type's soil moisture
                            // and 1 minute has passed then send a notification
                            for (Plant p : currentUser.getUserPlants())
                            {
                                if(currentTime - p.getLastNotificationTime() >= 60000)
                                {
                                    if(value.floatValue()>=(p.getActualSoilMoisture()+40))
                                    {
                                        // 1 min in milliseconds
                                        p.setLastNotificationTime(currentTime);
                                        notificationMessage = String.valueOf(p.getPlantName()) + " soil moisture's is too high, fix it!";
                                        showNotification(notificationId);
                                        notificationId++;

                                    } else if (value.floatValue()<=(p.getActualSoilMoisture())-40) {

                                         // 1 min in milliseconds
                                            p.setLastNotificationTime(currentTime);
                                            notificationMessage = String.valueOf(p.getPlantName()) + " soil moisture's is too low, fix it!";
                                            showNotification(notificationId);
                                            notificationId++;

                                    }
                                }

                            }
                        }

                    }

                    if(d.getKey().toLowerCase().contains("temperature"))
                    {
                        long currentTime = System.currentTimeMillis();
                        Number value = (Number)d.getValue();
                        //check if the user is logged in and has plants
                        if(currentUser != null && currentUser.getUserPlants().size()>0)
                        {
                            for (Plant p : currentUser.getUserPlants())
                            {
                                //for each plant if the plant's current temperature (sensor data)
                                // is higher by 5 + plant type's temperature or lower by 5 - plant type's temperature than plant type's temperature
                                // and 1 minute has passed then send a notification
                                if (currentTime - p.getLastNotificationTime() >= 60000)
                                {
                                    if(value.floatValue()>=(p.getActualTemp()+5))
                                    {
                                        p.setLastNotificationTime(currentTime);
                                        notificationMessage=String.valueOf(p.getPlantName())+" temperature's is too high, fix it!";
                                        showNotification(notificationId);
                                        notificationId++;


                                    } else if (value.floatValue()<=(p.getActualTemp()-5)) {

                                        p.setLastNotificationTime(currentTime);
                                        notificationMessage=String.valueOf(p.getPlantName())+" temperature's is too low, fix it!";
                                        showNotification(notificationId);
                                        notificationId++;
                                    }
                                }

                            }
                        }


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "The Green Guardian", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    // Create notification builder
    private NotificationCompat.Builder createNotificationBuilder() {
        System.out.println("I am in createNotificationBuilder() in notifications.");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Check your plant")
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder;
    }

    private void showNotification(int notificationId) {

        NotificationCompat.Builder builder = createNotificationBuilder();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        mNotificationManager.notify(notificationId, builder.build());

        Intent intent1 = new Intent(this,MainPage.class);
        intent1.putExtra("notificationId",notificationId);


    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("I am in onStartCommand in notifications.");
        if(intent !=null && intent.getExtras() != null)
        {
            Bundle bundle = intent.getExtras();
            currentUser=(UserProfile) bundle.getSerializable("currentProfile");
            if (currentUser != null)
            {

            }
        }


        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //thread.sleep for 1 min
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }


}