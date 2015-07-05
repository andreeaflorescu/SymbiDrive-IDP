package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoResponse;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zombie on 7/4/15.
 */
public class DriverProfileActivity extends ActionBarActivity {

    TextView tv_username;
    TextView tv_rating;
    TextView tv_car;
    Switch sw_smoking;
    Switch sw_music;
    ListView l_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_profile);
        Intent intent = getIntent();
        String socialID = intent.getStringExtra("socialID");

        initInterfaceElements();
        getUserInfoTask(socialID);

    }

    private void initInterfaceElements(){

        tv_username = (TextView)findViewById(R.id.tv_driver_name);
        tv_rating = (TextView)findViewById(R.id.tv_rating);
        tv_car = (TextView)findViewById(R.id.tv_car);
        sw_smoking = (Switch)findViewById(R.id.sw_smoking);
        sw_music = (Switch)findViewById(R.id.sw_music);
        l_feedback = (ListView)findViewById(R.id.list_feedback);

    }

    void getUserInfoTask(final String username){

        AsyncTask<Void, Void, SymbidriveUserInfoResponse> getUserInfoTask =
            new AsyncTask<Void, Void, SymbidriveUserInfoResponse>() {

                @Override
                protected SymbidriveUserInfoResponse doInBackground(Void... params) {

                    Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                    try {

                        SymbidriveUserInfoRequest getUserInfoRequest
                                = new SymbidriveUserInfoRequest();
                        getUserInfoRequest.setSocialID(username);


                        return apiServiceHandle.getUserInfo(getUserInfoRequest).execute();

                    } catch (IOException e) {
                        Log.e("symbi", "Exception during API call", e);
                        //showMessage(e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(SymbidriveUserInfoResponse response) {
                    if (response != null) {
                        updateView(response);
                    } else {
                        showMessage(getResources().getString(R.string.server_error_message));
                    }
                }
            };
        getUserInfoTask.execute();
    }

    private void updateView(SymbidriveUserInfoResponse response){

        if(response.getCar() != null){
            tv_car.setText(response.getCar());
        }
        if(response.getIsSmoker() != null){
            sw_smoking.setChecked(response.getIsSmoker());
            sw_smoking.setClickable(false);
        }
        if(response.getListenToMusic() != null){
            sw_music.setChecked(response.getListenToMusic());
            sw_music.setClickable(false);
        }
        if(response.getRating() != null && response.getRating() > 0){
            tv_rating.setText("Rating: " + response.getRating());
        }
        if(response.getUsername() != null){
            tv_username.setText(response.getUsername());
        }
        if(response.getFeedback() != null && response.getFeedback().size() > 0){
            List<String> list = response.getFeedback();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.feedback_text_view);
            for(int i = 0; i < list.size(); i++){
                adapter.add(list.get(i));
            }
            l_feedback.setAdapter(adapter);
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.feedback_text_view);
            adapter.add("This driver has no feedback yet.");
            l_feedback.setAdapter(adapter);
        }
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
