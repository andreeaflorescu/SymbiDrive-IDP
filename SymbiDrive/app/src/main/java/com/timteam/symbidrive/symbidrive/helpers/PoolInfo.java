package com.timteam.symbidrive.symbidrive.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoResponse;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zombie on 6/7/15.
 */
public class PoolInfo implements Serializable {
    @Override
    public String toString() {
        return "PoolInfo{" +
                "date=" + date +
                ", destinationPointLat=" + destinationPointLat +
                ", destinationPointLon=" + destinationPointLon +
                ", sourcePointLat=" + sourcePointLat +
                ", sourcePointLon=" + sourcePointLon +
                ", seats=" + seats +
                ", poolID=" + poolID +
                ", driverID='" + driverID + '\'' +
                ", driverUserName='" + driverUserName + '\'' +
                '}';
    }

    private DateTime date;
    private double destinationPointLat;
    private double destinationPointLon;
    private double sourcePointLat;
    private double sourcePointLon;
    private long seats;
    private long poolID;
    private String driverID;
    private List<String> passengers;
    private String driverUserName;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public double getDestinationPointLat() {
        return destinationPointLat;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }
    public List<String> getPassengers() {
        return this.passengers;
    }
    public void setDestinationPointLat(double destinationPointLat) {
        this.destinationPointLat = destinationPointLat;
    }

    public double getDestinationPointLon() {
        return destinationPointLon;
    }

    public void setDestinationPointLon(double destinationPointLon) {
        this.destinationPointLon = destinationPointLon;
    }

    public double getSourcePointLat() {
        return sourcePointLat;
    }

    public void setSourcePointLat(double sourcePointLat) {
        this.sourcePointLat = sourcePointLat;
    }

    public double getSourcePointLon() {
        return sourcePointLon;
    }

    public void setSourcePointLon(double sourcePointLon) {
        this.sourcePointLon = sourcePointLon;
    }

    public long getSeats() {
        return seats;
    }

    public void setSeats(long seats) {
        this.seats = seats;
    }

    public long getPoolID() {
        return poolID;
    }

    public void setPoolID(long poolID) {
        this.poolID = poolID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverUserName() {
        return driverUserName;
    }

    public void setDriverUserName(String driverUserName) {
        this.driverUserName = driverUserName;
    }

    public PoolInfo(DateTime dateTime,
                    double destinationPointLat,
                    double destinationPointLon,
                    double sourcePointLat,
                    double sourcePointLon,
                    long seats,
                    long poolID,
                    String driverID){

        this.date = dateTime;
        this.destinationPointLat = destinationPointLat;
        this.destinationPointLon = destinationPointLon;
        this.sourcePointLat = sourcePointLat;
        this.sourcePointLon = sourcePointLon;
        this.seats = seats;
        this.poolID = poolID;
        this.driverID = driverID;
        getUserName();

    }

    private void getUserName(){

        AsyncTask<Void, Void, SymbidriveUserInfoResponse> getUserInfo =
                new AsyncTask<Void, Void, SymbidriveUserInfoResponse> () {

                    @Override
                    protected SymbidriveUserInfoResponse doInBackground(Void... params) {

                        try {
                            Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();
                            SymbidriveUserInfoRequest userInfoRequest =
                                    new SymbidriveUserInfoRequest();

                            userInfoRequest.setSocialID(getDriverID());

                            return apiServiceHandle.getUserInfo(userInfoRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveUserInfoResponse response) {
                        if (response != null) {
                            Log.v("symbi", response.getUsername());
                            setDriverUserName(response.getUsername());
                        } else {
                            Log.v("symbi", "No greetings were returned by the API.");
                        }
                    }
                };

        getUserInfo.execute((Void) null);

    }
}
