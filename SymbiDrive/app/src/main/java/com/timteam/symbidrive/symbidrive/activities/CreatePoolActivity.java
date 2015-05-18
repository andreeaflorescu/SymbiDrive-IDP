package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.bustling_bay_88919.symbidrive.Symbidrive;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveCreatePoolRequest;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidrivePoolResponse;
import com.google.api.client.util.DateTime;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.CreatePoolFragment;
import com.timteam.symbidrive.symbidrive.fragments.DatePickerFragment;
import com.timteam.symbidrive.symbidrive.fragments.TimePickerFragment;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class CreatePoolActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_create_pool, new CreatePoolFragment())
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

    public void selectSavedRoute(View v){

    }

    public void createPool(View v){
        View view = getWindow().getDecorView();
        String source = ((EditText)view.findViewById(R.id.et_source_location))
                .getText().toString();
        String destination = ((EditText)view.findViewById(R.id.et_destination_location))
                .getText().toString();

        if(source.isEmpty() || destination.isEmpty()){
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                double[] sourceCoordinates = getCoordinates(source);
                double[] destinationCoordinates = getCoordinates(destination);
                long seats = 2;
                postPool(sourceCoordinates,
                        destinationCoordinates,
                        new DateTime(new Date()),
                        seats);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public double[] getCoordinates(String address) throws IOException {

        double[] coordinates = new double[2];

        Geocoder geocoder = new Geocoder(this.getApplicationContext());
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(address, 1);
        if(addresses.size() > 0) {
            coordinates[0] = addresses.get(0).getLatitude();
            coordinates[1] = addresses.get(0).getLongitude();
        }

        return coordinates;
    }

    public void postPool(final double[] sourceCoordinates,
                         final double[] destinationCoordinates,
                         final DateTime dateTime,
                         final long seats){

        AsyncTask<Void, Void, SymbidrivePoolResponse> getAndDisplayGreeting =
                new AsyncTask<Void, Void, SymbidrivePoolResponse> () {

                    @Override
                    protected SymbidrivePoolResponse doInBackground(Void... integers) {
                        // Retrieve service handle.
                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveCreatePoolRequest createPoolRequest =
                                    new SymbidriveCreatePoolRequest();
                            createPoolRequest.setDate(dateTime);
                            createPoolRequest.setDriverId("1234");
                            createPoolRequest.setSeats(seats);
                            createPoolRequest.setSourcePointLat(sourceCoordinates[0]);
                            createPoolRequest.setSourcePointLon(sourceCoordinates[1]);
                            createPoolRequest.setDestinationPointLat(destinationCoordinates[0]);
                            createPoolRequest.setDestinationPointLon(destinationCoordinates[1]);

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

        getAndDisplayGreeting.execute((Void)null);
    }

    public void openPoolsActivity(){
        Intent openPoolsActivity = new Intent(this, MainActivity.class);
        openPoolsActivity.putExtra("section", 1);
        startActivity(openPoolsActivity);
    }

}
