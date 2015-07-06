package com.timteam.symbidrive.symbidrive.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreatePoolRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreateRouteRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreateRouteResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.HomeFragment;
import com.timteam.symbidrive.symbidrive.fragments.NavigationDrawerFragment;
import com.timteam.symbidrive.symbidrive.fragments.ViewPoolsFragment;
import com.timteam.symbidrive.symbidrive.fragments.ProfileFragment;
import com.timteam.symbidrive.symbidrive.fragments.RegisterRouteFragment;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.CreateRouteTask;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;
import com.timteam.symbidrive.symbidrive.listeners.SaveCoordinatesListener;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private         Fragment fragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private LocationManager locationManager;
    private SaveCoordinatesListener saveCoordinatesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        if(getIntent().getExtras() != null){
            onNavigationDrawerItemSelected(getIntent().getExtras().getInt("section"));
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {


        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(position) {
            default:
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ViewPoolsFragment();
                break;
            case 2:
                fragment = new RegisterRouteFragment();
                break;
            case 3:
                fragment = new ProfileFragment();
                break;

        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_home);
                break;
            case 1:
                mTitle = getString(R.string.title_pools);
                break;
            case 2:
                mTitle = getString(R.string.title_register_route);
                break;
            case 3:
                mTitle = getString(R.string.title_profile);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        //Do not allow back button press
    }

    private void disableButton(Button button) {
        button.setAlpha((float) 0.4);
        button.setEnabled(false);
    }

    private void enableButton(Button button) {
        button.setAlpha((float) 1);
        button.setEnabled(true);
    }


    public void startGPSTracking(View view) {
        FragmentManager fm = fragment.getChildFragmentManager();
        SupportMapFragment map_fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (map_fragment == null) {
            map_fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, map_fragment).commit();
        }

        GoogleMap map = map_fragment.getMap();


        saveCoordinatesListener = new SaveCoordinatesListener(map);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, saveCoordinatesListener);

        Button startTrackingButton = (Button) findViewById(R.id.btn_start_gps_tracking);
        Button stopTrackingButton = (Button) findViewById(R.id.btn_stop_gps_tracking);
        disableButton(startTrackingButton);
        enableButton(stopTrackingButton);
    }

    public void stopGPSTracking(View view) {
        Log.v("SymbiDrive", "User clicked on Stop GPS tracking");
        if (saveCoordinatesListener != null) {
            Log.v("SymbiDrive", saveCoordinatesListener.getLocations().toString());
            locationManager.removeUpdates(saveCoordinatesListener);
        }

        Button startTrackingButton = (Button) findViewById(R.id.btn_start_gps_tracking);
        Button stopTrackingButton = (Button) findViewById(R.id.btn_stop_gps_tracking);
        disableButton(stopTrackingButton);
        enableButton(startTrackingButton);

        createCustomDialog();
    }

    public void createCustomDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.layout_dialog_save_route, null);
        // set title
        alertDialogBuilder.setView(view);

        // set dialog message
        alertDialogBuilder
                .setTitle("Save Route")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ArrayList<Double> locations_lat = new ArrayList<Double>();
                        ArrayList<Double> locations_lon = new ArrayList<Double>();
                        String route_name = ((EditText) view.findViewById(R.id.et_route_name)).getText().toString();
                        String socialID = SocialNetworkManager.getInstance().getSocialTokenID();
                        ArrayList<Location> locations = saveCoordinatesListener.getLocations();
                        for (Location location : locations) {
                            locations_lon.add(location.getLongitude());
                            locations_lat.add(location.getLatitude());
                        }
                        CreateRouteTask createRouteTask = new CreateRouteTask(route_name, socialID, locations_lat, locations_lon);
                        createRouteTask.execute();
                        Log.v("coordonate", saveCoordinatesListener.getLocations().toString());
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
