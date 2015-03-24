package com.timteam.symbidrive.symbidrive;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by zombie on 3/24/15.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    MainActivity mainActivity;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_navigation_home, container, false);
        initInterfaceElements(rootView);
        return rootView;
    }

    private void initInterfaceElements(View rootView){

        Button driver = (Button)rootView.findViewById(R.id.driverButton);
        driver.setOnClickListener(this);

        Button passenger = (Button)rootView.findViewById(R.id.passengerButton);
        passenger.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(getResources().
                getInteger(R.integer.home_section_id));
    }

    @Override
    public void onClick(View v) {

        Fragment newFragment = null;
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

        if(v.getId() == R.id.driverButton){
            newFragment = new DriverFragment();
        }
        if(v.getId() == R.id.passengerButton){
            newFragment = new PassengerFragment();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .addToBackStack(null)
                .commit();
    }
}
