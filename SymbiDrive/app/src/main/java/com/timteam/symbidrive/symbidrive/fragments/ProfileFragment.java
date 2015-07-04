package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.LoginActivity;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.facebook.login.LoginManager;

/**
 * Created by zombie on 3/22/15.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private SocialNetworkManager socialNetworkManager;

    private GoogleApiClient mGoogleApiClient;

    private Button btn_logout;

    private boolean music = false;
    private boolean smoking = false;

    private String telephone;
    private String carType;

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

        return rootView;
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
            executeLoginTask(v);
        }
    }

    private void executeLoginTask(View v){

        if(((EditText)v.findViewById(R.id.et_telephone)).getText() != null){
            telephone = ((EditText)v.findViewById(R.id.et_telephone)).getText().toString();
        }
        if(((EditText)v.findViewById(R.id.et_car_type)).getText() != null){
            carType = ((EditText)v.findViewById(R.id.et_car_type)).getText().toString();
        }



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
