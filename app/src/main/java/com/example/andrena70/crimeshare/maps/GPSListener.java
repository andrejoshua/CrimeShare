package com.example.andrena70.crimeshare.maps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by andre.na70 on 1/3/2016.
 */
public class GPSListener implements LocationListener {

    private double locationThresold = 0.0f;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    public void onLocationChanged(Location location) {
        this.currentLatitude = location.getLatitude();
        this.currentLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }
}
