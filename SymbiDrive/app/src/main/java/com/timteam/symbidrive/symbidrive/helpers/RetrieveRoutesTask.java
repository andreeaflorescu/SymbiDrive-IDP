package com.timteam.symbidrive.symbidrive.helpers;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveGetRoutesRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveGetRoutesResponse;
import com.timteam.symbidrive.symbidrive.R;

import java.io.IOException;

/**
 * Created by andreea on 05.07.2015.
 */
public class RetrieveRoutesTask extends AsyncTask<Integer, Void, SymbidriveGetRoutesResponse> {
    String socialID;
    View rootView;

    public RetrieveRoutesTask(String socialID, View rootView) {
        this.socialID  = socialID;
        this.rootView = rootView;
    }

    @Override
    protected SymbidriveGetRoutesResponse doInBackground(Integer... params) {
        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();
        try {
            SymbidriveGetRoutesRequest request = new SymbidriveGetRoutesRequest();
            request.setDriverSocialID(socialID);
            return apiServiceHandle.getRoutes(request).execute();
        } catch (IOException e) {
            Log.e("symbi", "Exception during API call", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(SymbidriveGetRoutesResponse symbidriveGetRoutesResponse) {
        if (symbidriveGetRoutesResponse == null) {

        } else {
            Log.v("===========", symbidriveGetRoutesResponse.toString());
//        Spinner spinner = (Spinner) mRootView.findViewById(R.id.spinner);
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, route_names);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
//        spinner.setAdapter(spinnerArrayAdapter);
        }
    }
}
