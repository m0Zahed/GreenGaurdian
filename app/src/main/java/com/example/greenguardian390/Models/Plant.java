//Plant: This class defines the plant object along with the lastNotificationTime used in sensorChangeNotifications
package com.example.greenguardian390.Models;

import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;

public class Plant implements Serializable {

    public float actualSoilMoisture,actualTemp;

    public long lastNotificationTime;

    public String plantName;


    public Plant()
    {

    }

    public Plant(float s, float t, String n) {
        this.actualSoilMoisture=s;
        this.actualTemp=t;
        this.plantName=n;
        this.lastNotificationTime=0;
    }

    public float getActualSoilMoisture() {
        return actualSoilMoisture;
    }

    public void setActualSoilMoisture(float actualSoilMoisture) {
        this.actualSoilMoisture = actualSoilMoisture;
    }

    public float getActualTemp() {
        return actualTemp;
    }

    public void setActualTemp(float actualTemp) {
        this.actualTemp = actualTemp;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setLastNotificationTime(long t)
    {
        this.lastNotificationTime=t;
    }

    public long getLastNotificationTime()
    {
        return lastNotificationTime;
    }

}
