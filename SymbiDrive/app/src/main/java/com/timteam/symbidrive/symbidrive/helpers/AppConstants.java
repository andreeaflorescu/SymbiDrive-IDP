package com.timteam.symbidrive.symbidrive.helpers;

/**
 * Created by andreea on 16.05.2015.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.appspot.symbidrive_997.symbidrive.Symbidrive;

import java.io.IOException;

import javax.annotation.Nullable;

public class AppConstants {

    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    /**
     * Class instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();


    /**
     * Retrieves a Symbidrive api service handle to access the API.
     */
    public static Symbidrive getApiServiceHandle() {
        // Use a builder to help formulate the API request.

        Symbidrive.Builder symbidrive = new Symbidrive.Builder(AppConstants.HTTP_TRANSPORT,
                AppConstants.JSON_FACTORY, null)
                .setRootUrl("https://1-dot-symbidrive-997.appspot.com/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {

                    @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                            throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });
        ;

        return symbidrive.build();
    }

}
