package com.timteam.symbidrive.symbidrive.listeners;

import android.app.Activity;
import android.content.Intent;

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

    private SocialNetworkManager socialNetworkManager;

    @Override
    public void onSuccess(LoginResult loginResult) {

        socialNetworkManager = SocialNetworkManager.getInstance();
        socialNetworkManager.setSocialNetworkID(
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
