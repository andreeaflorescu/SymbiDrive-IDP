package com.timteam.symbidrive.symbidrive.helpers;

import com.google.api.client.util.DateTime;

import java.io.Serializable;

/**
 * Created by zombie on 6/7/15.
 */
public class PoolInfo implements Serializable {

    private DateTime date;
    private double destinationPointLat;
    private double destinationPointLon;
    private double sourcePointLat;
    private double sourcePointLon;
    private long seats;
    private long poolID;
    private String driverID;

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
    }
}
