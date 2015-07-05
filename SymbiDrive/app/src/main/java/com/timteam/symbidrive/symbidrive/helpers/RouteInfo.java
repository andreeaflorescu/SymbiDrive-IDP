package com.timteam.symbidrive.symbidrive.helpers;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by andreea on 05.07.2015.
 */
public class RouteInfo {

    private final String name;
    private final ArrayList<LatLng> route_points;

    public RouteInfo(String name, ArrayList<LatLng> route_points) {

        this.name = name;
        this.route_points = route_points;
    }


    public String getName() {
        return name;
    }

    public ArrayList<LatLng> getRoute_points() {
        return route_points;
    }
}
