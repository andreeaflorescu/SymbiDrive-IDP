package com.timteam.symbidrive.symbidrive.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveAddFeedbackRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveAddRatingRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserResponse;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.CreatedPoolDetailsFragment;
import com.timteam.symbidrive.symbidrive.fragments.JoinedPoolDetailsFragment;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import java.io.IOException;

public class CreatedPoolDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_pool_details);

        String type = getIntent().getStringExtra("type");

        if (type.equals( "created")) {
            CreatedPoolDetailsFragment fragment = new CreatedPoolDetailsFragment();
            fragment.setArguments(getIntent().getExtras());
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment)
                        .commit();
            }
        } else {
            JoinedPoolDetailsFragment fragment = new JoinedPoolDetailsFragment();
            fragment.setArguments(getIntent().getExtras());
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment)
                        .commit();
            }
        }


    }

    private void postRating(final Integer rating) {
        AsyncTask<Void, Void, SymbidriveUserResponse> addRatingTask =
                new AsyncTask<Void, Void, SymbidriveUserResponse>() {

                    @Override
                    protected SymbidriveUserResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveAddRatingRequest addRatingRequest
                                    = new SymbidriveAddRatingRequest();
                            addRatingRequest.setSocialID(getIntent().getStringExtra("driverID"));
                            addRatingRequest.setRating(rating.longValue());


                            return apiServiceHandle.addRating(addRatingRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);
                            //showMessage(e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveUserResponse response) {
                        if (response != null) {
                             createCustomAlertDialog("Rating was added!");
                        }
                    }
                };
        addRatingTask.execute();
    }

    private void postFeedback(final String feedback) {
        AsyncTask<Void, Void, SymbidriveUserResponse> addFeedbackTask =
                new AsyncTask<Void, Void, SymbidriveUserResponse>() {

                    @Override
                    protected SymbidriveUserResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveAddFeedbackRequest addFeedbackRequest
                                    = new SymbidriveAddFeedbackRequest();
                            addFeedbackRequest.setSocialID(getIntent().getStringExtra("driverID"));
                            addFeedbackRequest.setFeedback(feedback);


                            return apiServiceHandle.addFeedback(addFeedbackRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);
                            //showMessage(e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveUserResponse response) {
                        if (response != null) {
                            createCustomAlertDialog("Feedback was added!");
                        }
                    }
                };
        addFeedbackTask.execute();
    }

    private void createCustomAlertDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void addFeedbackAndRating(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String s_rating = spinner.getSelectedItem().toString();
        String feedback = ((TextView)findViewById(R.id.et_feedback)).getText().toString();
        Integer rating = Integer.parseInt(s_rating);
        if (rating != 0) {
            postRating(rating);
        }

        if (!feedback.isEmpty()) {
            postFeedback(feedback);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pool_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
