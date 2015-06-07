package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appspot.bustling_bay_88919.symbidrive.Symbidrive;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveRegisterUserRequest;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveUserResponse;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.timteam.symbidrive.symbidrive.GetGoogleInfoTask;
import com.timteam.symbidrive.symbidrive.R;

import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;
import com.timteam.symbidrive.symbidrive.listeners.FacebookLoginCallback;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


public class LoginActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;



    private void initializeSocialNetworks() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSocialNetworks();
        updateWithToken(AccessToken.getCurrentAccessToken());

        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_google_login).setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.btn_facebook_login);
        loginButton.setOnClickListener(this);
        loginButton.registerCallback(callbackManager, new FacebookLoginCallback(this));
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        // check if user is logged in
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void openMainPage(String socialNetworkID){
        SocialNetworkManager.getInstance().setSocialNetworkID(socialNetworkID);
        Intent mainPageIntent = new Intent(this, MainActivity.class);
        startActivity(mainPageIntent);
    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    public void onClick(View view) {

        if (view.getId() == R.id.btn_google_login
                && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }

        if (view.getId() == R.id.btn_facebook_login) {
            LoginManager.getInstance().logInWithReadPermissions(this,
                    Arrays.asList("basic_info",
                            "user_friends",
                            "user_likes",
                            "user_birthday"));
        }
    }

    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        mSignInClicked = false;
        //Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        SocialNetworkManager.getInstance().setmGoogleApiClient(mGoogleApiClient);

        if(SocialNetworkManager.getInstance().getIsLoggedIn() == null ||
                !SocialNetworkManager.getInstance().getIsLoggedIn()){

            Object[] params = new Object[2];
            params[0] = this;
            params[1] = Plus.AccountApi.getAccountName(mGoogleApiClient);
            new GetGoogleInfoTask().execute(params);

            openMainPage(getResources().getString(R.string.google_profile));
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void updateWithToken(final AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            AsyncTask<Integer, Void, SymbidriveUserResponse> loginRequest =
                    new AsyncTask<Integer, Void, SymbidriveUserResponse> () {
                        @Override
                        protected SymbidriveUserResponse doInBackground(Integer... integers) {
                            // Retrieve service handle.
                            Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                            try {

                                SymbidriveRegisterUserRequest registerUserRequest = new SymbidriveRegisterUserRequest();
                                registerUserRequest.setDeviceID("13455");
                                registerUserRequest.setProfile("Facebook");
                                registerUserRequest.setSocialID(currentAccessToken.getToken());
                                registerUserRequest.setUsername("extraordinar");
                                return apiServiceHandle.registerUser(registerUserRequest).execute();

                            } catch (IOException e) {
                                Log.e("symbi", "Exception during API call", e);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(SymbidriveUserResponse response) {
                            if (response != null) {
                                Log.v("symbi", response.getRet());
                            } else {
                                Log.v("symbi", "No greetings were returned by the API.");
                            }
                        }


                    };

            loginRequest.execute(1);

            openMainPage(getResources().getString(R.string.facebook_profile));
        }
    }

}
