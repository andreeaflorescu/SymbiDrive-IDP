package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.symbidrive_997.symbidrive.*;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveRegisterUserRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUpdateUserInfoRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.LoginActivity;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.facebook.login.LoginManager;

import java.io.IOException;

/**
 * Created by zombie on 3/22/15.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private SocialNetworkManager socialNetworkManager;

    private GoogleApiClient mGoogleApiClient;

    private Button btn_logout;
    private Button btn_save;

    private boolean music = false;
    private boolean smoking = false;

    private String telephone = "";
    private String carType = "";

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_navigation_profile, container, false);
        socialNetworkManager = SocialNetworkManager.getInstance();

        setButtons(rootView);
        initializeSwitch(rootView);

        TextView username = ((TextView)rootView.findViewById(R.id.username));
        username.setText(socialNetworkManager.getUsername());

        ImageView profilePicture = ((ImageView)rootView.findViewById(R.id.profile_picture));
        profilePicture.setImageBitmap(socialNetworkManager.getProfilePicture());

        getUserInfoTask(rootView);

        return rootView;
    }

    void getUserInfoTask(final View rootView){

        AsyncTask<Void, Void, SymbidriveUserInfoResponse> getUserInfoTask =
                new AsyncTask<Void, Void, SymbidriveUserInfoResponse>() {

                    @Override
                    protected SymbidriveUserInfoResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveUserInfoRequest getUserInfoRequest
                                    = new SymbidriveUserInfoRequest();
                            getUserInfoRequest.setSocialID(socialNetworkManager.getSocialTokenID());


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
                            updateView(rootView, response);
                        } else {
                            showMessage(getResources().getString(R.string.server_error_message));
                        }
                    }
                };
        getUserInfoTask.execute();
    }

    private void updateView(View v, SymbidriveUserInfoResponse response){

        if(response.getTelephone() != null){
            ((EditText)v.findViewById(R.id.et_telephone)).setText(response.getTelephone());
        }
        if(response.getCar() != null){
            ((EditText)v.findViewById(R.id.et_car_type)).setText(response.getCar());
        }
        if(response.getIsSmoker() != null){
            ((Switch)v.findViewById(R.id.sw_smoking)).setChecked(response.getIsSmoker());
        }
        if(response.getListenToMusic() != null){
            ((Switch)v.findViewById(R.id.sw_music)).setChecked(response.getListenToMusic());
        }
        if(response.getRating() != null){
            ((TextView)v.findViewById(R.id.tv_rating)).setText("Rating: " + response.getRating());
        }
    }

    private void initializeSwitch(View rootView){

        Switch aSwitch = ((Switch)rootView.findViewById(R.id.sw_smoking));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                smoking = isChecked;
                Log.v("switch", String.valueOf(smoking));
            }
        });

        aSwitch = (Switch)rootView.findViewById(R.id.sw_music);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                music = isChecked;
                Log.v("switch", String.valueOf(music));
            }
        });
    }

    private void setButtons(View rootView){

        btn_logout = (Button)rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        btn_save = (Button)rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        if(socialNetworkManager.getSocialNetworkID().equals(
                getResources().getString(R.string.google_profile))){

            mGoogleApiClient = socialNetworkManager.getmGoogleApiClient();
            mGoogleApiClient.connect();
            btn_logout.setText("Log out from " + socialNetworkManager.getSocialNetworkID());
        }

        if(socialNetworkManager.getSocialNetworkID().equals(
                getResources().getString(R.string.facebook_profile).toString())){
            btn_logout.setText("Log out from " + "Facebook");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getResources().
                getInteger(R.integer.profile_section_id));
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_logout){
            logout();
        }
        if(v.getId() == R.id.btn_save){
            executeUpdateTask(v);
        }
    }

    private void executeUpdateTask(View v){

        try{
            telephone = ((EditText)v.findViewById(R.id.et_telephone)).getText().toString();
            carType = ((EditText)v.findViewById(R.id.et_car_type)).getText().toString();
        }
        catch (Exception ex){
            Log.v("null", "no text provided");
        }


        AsyncTask<Void, Void, SymbidriveUserResponse> saveRequest =
                new AsyncTask<Void, Void, SymbidriveUserResponse> () {

                    @Override
                    protected SymbidriveUserResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveUpdateUserInfoRequest updateUserInfoRequest
                                    = new SymbidriveUpdateUserInfoRequest();

                            updateUserInfoRequest.setSocialID(socialNetworkManager.getSocialTokenID());
                            updateUserInfoRequest.setUsername(socialNetworkManager.getUsername());
                            updateUserInfoRequest.setIsSmoker(smoking);
                            updateUserInfoRequest.setListenToMusic(music);
                            updateUserInfoRequest.setTelephone(telephone);
                            updateUserInfoRequest.setCar(carType);

                            return apiServiceHandle.updateUserProfile(updateUserInfoRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveUserResponse response) {
                        if (response != null) {
                            Log.v("symbi", response.getRet());
                            //TODO - verify results
                            showMessage(response.getRet());
                        } else {
                            showMessage(getResources().getString(R.string.server_error_message));
                        }
                    }
                };

        saveRequest.execute();

    }

    private void showMessage(String message){
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
    }

    private void logout(){
        if(socialNetworkManager.getSocialNetworkID().equals(
                getResources().getString(R.string.google_profile)) &&
                mGoogleApiClient.isConnected()) {

            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
        }

        if(socialNetworkManager.getSocialNetworkID().equals(
                getResources().getString(R.string.facebook_profile))){
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

}
