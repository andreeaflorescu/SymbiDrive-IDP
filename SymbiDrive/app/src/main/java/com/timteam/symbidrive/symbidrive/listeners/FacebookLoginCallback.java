package com.timteam.symbidrive.symbidrive.listeners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.timteam.symbidrive.symbidrive.SocialNetworkManager;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.timteam.symbidrive.symbidrive.R;

/**
 * Created by andreea on 26.03.2015.
 */
public class FacebookLoginCallback implements FacebookCallback<LoginResult> {
    private Activity loginActivity;

    public FacebookLoginCallback(Activity activity) {
        this.loginActivity = activity;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        String userID = loginResult.getAccessToken().getUserId();
        Log.d("facebook userID", userID);

        SocialNetworkManager.getInstance().setSocialNetworkID(
                loginActivity.getResources().getString(R.string.facebook_profile));

        Intent intent = new Intent(loginActivity.getApplicationContext(), MainActivity.class);
        loginActivity.startActivity(intent);
    }

    @Override
    public void onCancel() {
        // App code
    }

    @Override
    public void onError(FacebookException exception) {
        // App code
    }
}
