package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appspot.bustling_bay_88919.symbidrive.Symbidrive;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveRegisterUserRequest;
import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveUserResponse;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.timteam.symbidrive.symbidrive.GetGoogleInfoTask;
import com.timteam.symbidrive.symbidrive.R;

import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;
import com.timteam.symbidrive.symbidrive.listeners.FacebookLoginCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import android.provider.Settings.Secure;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


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

    private SocialNetworkManager socialNetworkManager;

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

        socialNetworkManager = SocialNetworkManager.getInstance();
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
        socialNetworkManager.setSocialNetworkID(socialNetworkID);
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
            socialNetworkManager
                    .setSocialNetworkID(getResources().getString(R.string.google_profile));
            mSignInClicked = true;
            resolveSignInError();
        }

        if (view.getId() == R.id.btn_facebook_login) {
            socialNetworkManager
                    .setSocialNetworkID(getResources().getString(R.string.facebook_profile));
            LoginManager.getInstance().logInWithReadPermissions(this,
                    Arrays.asList("basic_info",
                            "user_likes",
                            "user_birthday"));
        }
    }

    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        if(socialNetworkManager.getSocialNetworkID() ==
                getResources().getString(R.string.google_profile)){
            mSignInClicked = false;
            //Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
            socialNetworkManager.setmGoogleApiClient(mGoogleApiClient);

            if(socialNetworkManager.getIsLoggedIn() == null ||
                    !socialNetworkManager.getIsLoggedIn()){

                Object[] params = new Object[2];
                params[0] = this;
                params[1] = Plus.AccountApi.getAccountName(mGoogleApiClient);
                new GetGoogleInfoTask().execute(params);

                openMainPage(getResources().getString(R.string.google_profile));
            }
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

        if (requestCode == RC_SIGN_IN &&
                socialNetworkManager.getSocialNetworkID() ==
                        getResources().getString(R.string.google_profile)) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        if(socialNetworkManager.getSocialNetworkID() ==
                getResources().getString(R.string.facebook_profile)){
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
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

            socialNetworkManager.setSocialTokenID(currentAccessToken.getToken());
            socialNetworkManager.setSocialNetworkID(getString(R.string.facebook_profile));
            socialNetworkManager
                    .setSocialNetworkID(getResources().getString(R.string.facebook_profile));
            openMainPage("facebook");

            callFacebookGraph(currentAccessToken);
        }
    }

    private void callFacebookGraph(final AccessToken accessToken){

        String username;

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            parseFacebookResponse(response.getJSONObject());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void parseFacebookResponse(final JSONObject response) throws JSONException,
            IOException {

        socialNetworkManager.setUsername(response.getString("name"));

        AsyncTask<Void, Void, Void> getPhotoTask = new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                URL imgUrl = null;
                try {
                    imgUrl = new URL(response.getJSONObject("picture")
                            .getJSONObject("data").getString("url"));
                    InputStream in = (InputStream) imgUrl.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    socialNetworkManager.setProfilePicture(bitmap);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loginTask();
            }
        };
        getPhotoTask.execute();

    }

    private void loginTask(){


        final String android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        AsyncTask<Void, Void, SymbidriveUserResponse> loginRequest =
                new AsyncTask<Void, Void, SymbidriveUserResponse> () {

                    @Override
                    protected SymbidriveUserResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {
                            SymbidriveRegisterUserRequest registerUserRequest =
                                    new SymbidriveRegisterUserRequest();

                            registerUserRequest.setDeviceID(android_id);
                            registerUserRequest.setProfile(socialNetworkManager.getSocialNetworkID());
                            registerUserRequest.setSocialID(socialNetworkManager.getSocialTokenID());
                            registerUserRequest.setUsername(socialNetworkManager.getUsername());

                            return apiServiceHandle.registerUser(registerUserRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);
                            showMessage(e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveUserResponse response) {
                        if (response != null) {
                            Log.v("symbi", response.getRet());
                            //TODO - verify results
                            showMessage("Welcome " + socialNetworkManager.getUsername());
                            openMainPage(socialNetworkManager.getSocialNetworkID());
                        } else {
                            showMessage("Seems like something went wrong on server. Please try again");
                        }
                    }
                };

        loginRequest.execute();
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }

}
