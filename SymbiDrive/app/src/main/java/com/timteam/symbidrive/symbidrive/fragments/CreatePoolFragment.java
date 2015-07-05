package com.timteam.symbidrive.symbidrive.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreatePoolRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveGetRoutesRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveGetRoutesResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidrivePoolResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveRegisterUserRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserResponse;
import com.google.api.client.util.DateTime;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.adapters.PlacesAutoCompleteAdapter;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;
import com.timteam.symbidrive.symbidrive.helpers.RetrieveRoutesTask;
import com.timteam.symbidrive.symbidrive.helpers.RouteInfo;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zombie on 3/24/15.
 */
public class CreatePoolFragment extends Fragment{

    View mRootView;
    private RouteInfo[] routeInfos;
    public CreatePoolFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_pool, container, false);
        postGetRoutesRequest();
        mRootView = rootView;
        return rootView;
    }

    private void initSpinner(String[] route_names) {
        Spinner spinner = (Spinner) mRootView.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, route_names);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

    }

    private void postGetRoutesRequest() {
        AsyncTask<Void, Void, SymbidriveGetRoutesResponse> task = new AsyncTask<Void, Void, SymbidriveGetRoutesResponse>() {
            @Override
            protected SymbidriveGetRoutesResponse doInBackground(Void... params) {
                Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();
                try {
                    SymbidriveGetRoutesRequest request = new SymbidriveGetRoutesRequest();
                    request.setDriverSocialID( SocialNetworkManager.getInstance().getSocialTokenID());
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
                    routeInfos = DataManager.getRoutesInfoFromRouteResponse(symbidriveGetRoutesResponse.getRoutes());
                    String[] route_names = DataManager.getRoutesName(routeInfos);
                    initSpinner(route_names);
                }
            }
        };
        task.execute();
    }

}
