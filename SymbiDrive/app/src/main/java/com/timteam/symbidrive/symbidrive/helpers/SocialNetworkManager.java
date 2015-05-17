package com.timteam.symbidrive.symbidrive.helpers;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by zombie on 3/30/15.
 */
public class SocialNetworkManager {

    private static SocialNetworkManager instance = null;

    private String socialNetworkID;

    private GoogleApiClient mGoogleApiClient;

    private Boolean isLoggedIn;

    public SocialNetworkManager(){}

    public static SocialNetworkManager getInstance(){
        if(instance == null){
            instance = new SocialNetworkManager();
        }
        return instance;
    }

    public void setSocialNetworkID(String id){
        this.socialNetworkID = id;
    }

    public String getSocialNetworkID(){
        return this.socialNetworkID;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}
