package com.timteam.symbidrive.symbidrive.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreateRouteRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreateRouteResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by andreea on 27.06.2015.
 */

public class CreateRouteTask extends AsyncTask<Integer, Void, SymbidriveCreateRouteResponse> {
    private String routeName;
    private String socialId;
    private ArrayList<Double> lats, lons;

    public CreateRouteTask(String routeName, String socialId, ArrayList<Double> lats, ArrayList<Double> lons) {
        this.routeName = routeName;
        this.socialId = socialId;
        this.lats = lats;
        this.lons = lons;
    }

    @Override
    protected SymbidriveCreateRouteResponse doInBackground(Integer... params) {
        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();
        try {

            SymbidriveCreateRouteRequest registerRouteRequest =
                    new SymbidriveCreateRouteRequest();
            registerRouteRequest.setDriverSocialID(socialId);
            registerRouteRequest.setName(routeName);
            registerRouteRequest.setRoutePointsLat(lats);
            registerRouteRequest.setRoutePointsLong(lons);

            return apiServiceHandle.createRoute(registerRouteRequest).execute();

        } catch (IOException e) {
            Log.e("symbi", "Exception during API call", e);
        }
        return null;
    }
}
