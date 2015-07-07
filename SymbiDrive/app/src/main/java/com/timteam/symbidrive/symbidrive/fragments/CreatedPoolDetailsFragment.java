package com.timteam.symbidrive.symbidrive.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.DirectionsJSONParser;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andreea on 04.07.2015.
 */
public class CreatedPoolDetailsFragment extends Fragment {

    private SupportMapFragment fragment;
    private GoogleMap map;
    private View rootView;
    public CreatedPoolDetailsFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_view_pool);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_view_pool, fragment).commit();
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            Bundle bundle = this.getArguments();
            double  destinationPointLat = bundle.getDouble("destinationPointLat", 0);
            double  destinationPointLon = bundle.getDouble("destinationPointLon", 0);
            double sourcePointLat = bundle.getDouble("sourcePointLat", 0);
            double sourcePointLon = bundle.getDouble("sourcePointLon", 0);

            LatLng source = new LatLng(sourcePointLat, sourcePointLon);
            LatLng destination = new LatLng(destinationPointLat, destinationPointLon);
            map.addMarker(new MarkerOptions()
                    .position(source)
                    .title("Source"));
            map.addMarker(new MarkerOptions()
                    .position(destination)
                    .title("Destination"));
            String url = getDirectionsUrl(source, destination);
            new DownloadTask().execute(url);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sourcePointLat, sourcePointLon), 10));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_created_pool_details, container, false);

        Bundle data = this.getArguments();
        String[] passengersIDS = data.getStringArray("passengersIDS");
        LinearLayout passengersContainer = (LinearLayout) rootView.findViewById(R.id.layout_passengers_container);
        if (passengersIDS != null) {
            updatePassengersInfo(passengersIDS);
        } else {

            TextView noPassengers = new TextView(this.getActivity().getApplicationContext());
            noPassengers.setText("No passengers joined this pool");
            noPassengers.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            params.setMargins(0,10,0,0);
            noPassengers.setLayoutParams(params);
            passengersContainer.addView(noPassengers);
        }
        return rootView;
    }

    private void updatePassengersInfo(String[] passengersIDS) {
        for (String id : passengersIDS) {
            getUserInfoTask(id);
        }

    }

    private void addPassengerLayout(String passengerName, String passengerTelephone) {
        LinearLayout passengersContainer = (LinearLayout) rootView.findViewById(R.id.layout_passengers_container);
        Context context = this.getActivity().getApplicationContext();
        LinearLayout passengerLayout = new LinearLayout(context);
        TextView name = new TextView(context);
        name.setText(passengerName);
        name.setTextColor(Color.BLACK);

        TextView blank = new TextView(context);
        blank.setWidth(10);

        TextView telephone = new TextView(context);
        telephone.setText(passengerTelephone);
        telephone.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        telephone.setClickable(true);

        passengerLayout.addView(name);
        passengerLayout.addView(blank);
        passengerLayout.addView(telephone);

        passengersContainer.addView(passengerLayout);

    }

    void getUserInfoTask(final String id){

        AsyncTask<Void, Void, SymbidriveUserInfoResponse> getUserInfoTask =
                new AsyncTask<Void, Void, SymbidriveUserInfoResponse>() {

                    @Override
                    protected SymbidriveUserInfoResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveUserInfoRequest getUserInfoRequest
                                    = new SymbidriveUserInfoRequest();
                            getUserInfoRequest.setSocialID(id);


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
                            addPassengerLayout(response.getUsername(), response.getTelephone());
                        } else {

                        }
                    }
                };
        getUserInfoTask.execute();
    }


    /** A method to download json data from url */

    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e)
        {
            Log.d("Exception", e.toString());
        } finally
        {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>
    {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url)
        {

            // For storing data from web service
            String data = "";

            try
            {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try
            {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result)
        {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";



            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++)
            {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++)
                {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0)
                    { // Get distance from the list
                        distance = point.get("distance");
                        continue;
                    } else if (j == 1)
                    { // Get duration from the list
                        duration = point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route

            map.addPolyline(lineOptions);
        }
    }
}
