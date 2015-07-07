package com.timteam.symbidrive.symbidrive.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.appspot.symbidrive_997.symbidrive.model.SymbidriveGetRoutesResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidrivePoolResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveSingleGetRoutesResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveSinglePoolResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zombie on 6/7/15.
 */
public class DataManager {

    public static double[] getCoordinates(String address, Context context) throws IOException {

        double[] coordinates = new double[2];

        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(address, 1);
        if(addresses.size() > 0) {
            coordinates[0] = addresses.get(0).getLatitude();
            coordinates[1] = addresses.get(0).getLongitude();
        }

        return coordinates;
    }

    public static String getAddress(double latitude, double longitude, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        addresses = geocoder.getFromLocation(latitude,longitude, 1);
        String address = new String();
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0).getAddressLine(0);
        }

        return address;
    }

    public static DateTime getDateTime(String dateValue, String timeValue){

        String dateTimeValue = "";

        String[] date = dateValue.split("/");
        String[] time = timeValue.split(":");

        dateTimeValue += date[2] + "-";
        if(date[1].length() == 1){
            dateTimeValue += "0" + date[1] + "-";
        }
        else {
            dateTimeValue += date[1] + "-";
        }
        if(date[0].length() == 1){
            dateTimeValue += "0" + date[0] + "T";
        }
        else {
            dateTimeValue += date[0] + "T";
        }

        if(time[0].length() == 1){
            dateTimeValue += "0" + time[0] + ":";
        }
        else {
            dateTimeValue += time[0] + ":";
        }

        if(time[1].length() == 1){
            dateTimeValue += "0" + time[1] + ":00Z";
        }
        else {
            dateTimeValue += time[1] + ":00Z";
        }

        return new DateTime(dateTimeValue);
    }


    public static PoolInfo[] getPoolInfoFromPoolResponse(List<SymbidriveSinglePoolResponse> response) {
        PoolInfo[] poolsInfo = new PoolInfo[response.size()];
        for (int i = 0; i < response.size(); i++) {
            SymbidriveSinglePoolResponse pool = response.get(i);
            poolsInfo[i] = new PoolInfo(pool.getDate(),
                    pool.getDestinationPointLat(),
                    pool.getDestinationPointLon(),
                    pool.getSourcePointLat(),
                    pool.getSourcePointLon(),
                    pool.getSeats(),
                    pool.getPoolId(),
                    pool.getDriverId());
            poolsInfo[i].setPassengers(pool.getPassengersIds());
        }

        return poolsInfo;
    }

    public static ArrayList<LatLng> getPointsFromLatLon(List<Double> lat, List<Double> lon) {
        ArrayList<LatLng> res = new ArrayList<>(lat.size());
        for (int i = 0; i < lat.size(); i++) {
            res.add(new LatLng(lat.get(i), lon.get(i)));
        }
        return res;
    }

    public static RouteInfo[] getRoutesInfoFromRouteResponse(List<SymbidriveSingleGetRoutesResponse> response) {
        if (response != null) {
            RouteInfo[] routeInfos = new RouteInfo[response.size()];
            for (int i = 0; i < response.size();i++) {
                SymbidriveSingleGetRoutesResponse routes = response.get(i);
                routeInfos[i] = new RouteInfo(routes.getName(),routes.getId(), getPointsFromLatLon(routes.getRoutePointsLat(),
                        routes.getRoutePointsLong()));
            }
            return routeInfos;
        }
        return new RouteInfo[0];
    }

    public static String[] getRoutesName(RouteInfo[] routeInfos) {
        String[] names = new String[routeInfos.length];
        for (int i = 0; i < routeInfos.length; i++) {
            names[i] = routeInfos[i].getName();
        }

        return names;
    }

}
