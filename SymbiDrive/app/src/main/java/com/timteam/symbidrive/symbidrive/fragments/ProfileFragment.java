package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.timteam.symbidrive.symbidrive.R;
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

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_profile, container, false);

        btn_logout = (Button)rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        socialNetworkManager = SocialNetworkManager.getInstance();

        if(socialNetworkManager.getSocialNetworkID() ==
                getResources().getString(R.string.google_profile)){

            mGoogleApiClient = socialNetworkManager.getmGoogleApiClient();
            mGoogleApiClient.connect();
            btn_logout.setText("Log out from " + socialNetworkManager.getSocialNetworkID());
        }

        if(socialNetworkManager.getSocialNetworkID() ==
                getResources().getString(R.string.facebook_profile)){
            btn_logout.setText("Log out from " + socialNetworkManager.getSocialNetworkID());
        }

        return rootView;
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

            if(socialNetworkManager.getSocialNetworkID() ==
                    getResources().getString(R.string.google_profile) &&
                    mGoogleApiClient.isConnected()) {

                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            btn_logout.setText("Login");
                        }
                });
            }

            if(socialNetworkManager.getSocialNetworkID() ==
                    getResources().getString(R.string.facebook_profile)){
                LoginManager.getInstance().logOut();
                btn_logout.setText("Login");
            }
        }
    }
}
