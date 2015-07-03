package com.timteam.symbidrive.symbidrive.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.bustling_bay_88919.symbidrive.Symbidrive;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveFindPoolRequest;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveFindPoolResponse;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveSinglePoolResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.util.DateTime;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.CreateScheduleLeaveFragment;
import com.timteam.symbidrive.symbidrive.fragments.DatePickerFragment;
import com.timteam.symbidrive.symbidrive.fragments.FindPoolFragment;
import com.timteam.symbidrive.symbidrive.fragments.FindScheduleLeaveFragment;
import com.timteam.symbidrive.symbidrive.fragments.MapSearchFragment;
import com.timteam.symbidrive.symbidrive.fragments.TimePickerFragment;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FindPoolActivity extends ActionBarActivity{
    private MapSearchFragment mapSearchFragment;
    private LatLng source;
    private LatLng destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pool);

        mapSearchFragment = new MapSearchFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_find_pool, mapSearchFragment)
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

            FindScheduleLeaveFragment fragment = new FindScheduleLeaveFragment();

            findViewById(R.id.fragment_map_search).setVisibility(View.INVISIBLE);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_find_pool, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment;
        newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void findMatchingPools(View v) throws IOException {

        View view = getWindow().getDecorView();
        String dateValue = ((TextView)view.findViewById(R.id.tv_date_picker))
                .getText().toString();
        String timeValue = ((TextView)view.findViewById(R.id.tv_time_picker))
                .getText().toString();
        CharSequence walkingDistance = ((TextView) view.findViewById(R.id.et_walking_distance))
                .getText();

        if (walkingDistance.length() == 0) {
            createCustomAlertDialog("Please fill in the walking distance!");
        } else {
            findPools(DataManager.getDateTime(dateValue, timeValue),
                    source,
                    destination,
                    Double.parseDouble(walkingDistance.toString()));
        }
    }

    private void findPools(final DateTime date,
                           final LatLng source,
                           final LatLng destination,
                           final double walkingDistance){

        AsyncTask<Void, Void, SymbidriveFindPoolResponse> findPoolTask =
            new AsyncTask<Void, Void, SymbidriveFindPoolResponse> () {

                @Override
                protected SymbidriveFindPoolResponse doInBackground(Void... params) {

                    try {
                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();
                        SymbidriveFindPoolRequest findPoolRequest = new SymbidriveFindPoolRequest();

                        findPoolRequest.setSocialID(
                                SocialNetworkManager.getInstance().getSocialTokenID());
                        findPoolRequest.setDate(date);
                        findPoolRequest.setDelta(date);
                        findPoolRequest.setStartPointLat(source.latitude);
                        findPoolRequest.setStartPointLon(source.longitude);
                        findPoolRequest.setEndPointLat(destination.latitude);
                        findPoolRequest.setEndPointLon(destination.longitude);
                        findPoolRequest.setWalkingDistance(walkingDistance);

                        return apiServiceHandle.findPool(findPoolRequest).execute();

                    } catch (IOException e) {
                        Log.e("symbi", "Exception during API call", e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(SymbidriveFindPoolResponse response) {
                    if (response != null) {
                        Log.v("symbi", response.toString());
                        openMatchingPoolsActivity(response);
                    } else {
                        Log.v("symbi", "No greetings were returned by the API.");
                    }
                }
            };

        findPoolTask.execute((Void) null);
    }

    private void openMatchingPoolsActivity(SymbidriveFindPoolResponse response) {

        if (response.getPools() == null) {
            Log.e("Symbidrive", "response e null");
        } else {
            List<SymbidriveSinglePoolResponse> pools = response.getPools();
            ArrayList<PoolInfo> poolsInfo = new ArrayList<PoolInfo>(pools.size());
            for (int i = 0; i < pools.size(); i++) {
                SymbidriveSinglePoolResponse pool = pools.get(i);
                poolsInfo.add(new PoolInfo(pool.getDate(),
                        pool.getDestinationPointLat(),
                        pool.getDestinationPointLon(),
                        pool.getSourcePointLat(),
                        pool.getSourcePointLon(),
                        pool.getSeats(),
                        pool.getPoolId(),
                        pool.getDriverId()));
            }

            Intent intent = new Intent(this, MatchingPoolsActivity.class);
            intent.putExtra("pools", poolsInfo);
            startActivity(intent);
        }
    }
}