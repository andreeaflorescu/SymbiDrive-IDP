package com.timteam.symbidrive.symbidrive;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

/**
 * Created by zombie on 4/23/15.
 */
public class GetGoogleInfoTask extends AsyncTask {

    private final static String PLUS_LOGIN_SCOPE = "https://www.googleapis.com/auth/plus.login";
    private final static String mScopes = "oauth2: " + PLUS_LOGIN_SCOPE;
    private String token;
    private Activity activity;

    @Override
    protected String doInBackground(Object[] params) {
        try {
            activity = (Activity)params[0];
            token = GoogleAuthUtil.getToken(activity,
                    (String)params[1],
                    mScopes);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        //super.onPostExecute(o);
        Log.d("Post", o.toString());
    }
}
