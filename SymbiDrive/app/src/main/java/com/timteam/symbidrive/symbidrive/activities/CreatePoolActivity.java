package com.timteam.symbidrive.symbidrive.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreatePoolRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidrivePoolResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.DateTime;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.CreatePoolFragment;
import com.timteam.symbidrive.symbidrive.fragments.CreateScheduleLeaveFragment;
import com.timteam.symbidrive.symbidrive.fragments.DatePickerFragment;
import com.timteam.symbidrive.symbidrive.fragments.MapSearchFragment;
import com.timteam.symbidrive.symbidrive.fragments.TimePickerFragment;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import java.io.IOException;


public class CreatePoolActivity extends FragmentActivity {
    private MapSearchFragment mapSearchFragment;

    private CreatePoolFragment createPoolFragment;
    private LatLng source;
    private LatLng destination;
    private Long routeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);
        createPoolFragment = new CreatePoolFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_create_pool, createPoolFragment)
                    .commit();
        }
    }

    private void createCustomAlertDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void inflateScheduleFragment(View view) {

        source = mapSearchFragment.getSourceLocation();
        destination = mapSearchFragment.getDestinationLocation();
        if (source == null || destination == null) {
            createCustomAlertDialog("Please choose source and destination points!");
        } else {

            CreateScheduleLeaveFragment fragment = new CreateScheduleLeaveFragment();

            findViewById(R.id.fragment_map_search).setVisibility(View.INVISIBLE);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_create_pool, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void inflateMapSearchFragment(View view) {
        mapSearchFragment = new MapSearchFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_create_pool, mapSearchFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void createPool(View v){

        View view = getWindow().getDecorView();
        CharSequence seats = ((EditText)view.findViewById(R.id.et_available_seats)).
                getText();
        String dateValue = ((TextView)view.findViewById(R.id.tv_date_picker)).
                getText().toString();
        String timeValue = ((TextView)view.findViewById(R.id.tv_time_picker)).
                getText().toString();

        if(seats.length() == 0){
            createCustomAlertDialog("Please fill the number of available seats!");
        } else {
            postPool(source,
                    destination,
                    routeID,
                    DataManager.getDateTime(dateValue, timeValue),
                    Long.parseLong(seats.toString()));
        }
    }

    public void postPool(final LatLng source, final LatLng destination, final Long route_id,
                         final DateTime dateTime,
                         final long seats){

        AsyncTask<Void, Void, SymbidrivePoolResponse> postPoolInfo =
                new AsyncTask<Void, Void, SymbidrivePoolResponse> () {

                    @Override
                    protected SymbidrivePoolResponse doInBackground(Void... integers) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveCreatePoolRequest createPoolRequest =
                                    new SymbidriveCreatePoolRequest();

                            createPoolRequest.setDate(dateTime);
                            createPoolRequest.setDriverId(SocialNetworkManager
                                    .getInstance()
                                    .getSocialTokenID());
                            createPoolRequest.setSeats(seats);
                            if (source != null && destination != null) {
                                createPoolRequest.setSourcePointLat(source.latitude);
                                createPoolRequest.setSourcePointLon(source.longitude);
                                createPoolRequest.setDestinationPointLat(destination.latitude);
                                createPoolRequest.setDestinationPointLon(destination.longitude);
                            } else {
                                createPoolRequest.setRouteId(routeID);
                            }

                            return apiServiceHandle.createPool(createPoolRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidrivePoolResponse response) {
                        if (response != null) {
                            Log.v("symbi", response.getRet());
                            openPoolsActivity();
                        } else {
                            Log.v("symbi", "No greetings were returned by the API.");
                        }
                    }
                };

        postPoolInfo.execute((Void) null);
    }

    public void selectRoute(View view) {
        CreateScheduleLeaveFragment fragment = new CreateScheduleLeaveFragment();
        routeID = createPoolFragment.getSelectedRouteID();
        if (findViewById(R.id.fragment_map_search) != null) {
            findViewById(R.id.fragment_map_search).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.fragment_create_pool).setVisibility(View.INVISIBLE);

        getFragmentManager().beginTransaction()
                .replace(R.id.container_create_pool, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void openPoolsActivity(){
        Intent openPoolsActivity = new Intent(this, MainActivity.class);
        openPoolsActivity.putExtra("section", 1);
        startActivity(openPoolsActivity);
    }

}
