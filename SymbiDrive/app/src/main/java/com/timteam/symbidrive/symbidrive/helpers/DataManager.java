package com.timteam.symbidrive.symbidrive.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.api.client.util.DateTime;

import java.io.IOException;
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

}
