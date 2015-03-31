package com.timteam.symbidrive.symbidrive.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by andreea on 31.03.2015.
 */
public class SaveCoordinatesListener implements LocationListener {

    private ArrayList<Location> locations = new ArrayList<>();
    @Override
    public void onLocationChanged(Location location) {
        locations.add(location);
        Log.v("SymbiDrive", "Added location to the array");
        Log.v("SymbiDrive", location.toString());
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

    public ArrayList<Location> getLocations() {
        return this.locations;
    }
}
