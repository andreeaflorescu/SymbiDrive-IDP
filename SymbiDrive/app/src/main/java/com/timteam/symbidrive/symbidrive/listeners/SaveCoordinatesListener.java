package com.timteam.symbidrive.symbidrive.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by andreea on 31.03.2015.
 */
public class SaveCoordinatesListener implements LocationListener {

    private ArrayList<Location> locations = new ArrayList<>();
    private GoogleMap map;

    public SaveCoordinatesListener(GoogleMap map) {
        this.map = map;
    }

    @Override
    public void onLocationChanged(Location location) {
        locations.add(location);
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        map.addMarker(new MarkerOptions()
                            .position(position));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
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
