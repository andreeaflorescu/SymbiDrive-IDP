package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.CreatePoolActivity;
import com.timteam.symbidrive.symbidrive.activities.FindPoolActivity;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;

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

        if(v.getId() == R.id.driverButton){
            Intent intent = new Intent(getActivity(), CreatePoolActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.passengerButton){
            Intent intent = new Intent(getActivity(), FindPoolActivity.class);
            startActivity(intent);
        }


    }
}
