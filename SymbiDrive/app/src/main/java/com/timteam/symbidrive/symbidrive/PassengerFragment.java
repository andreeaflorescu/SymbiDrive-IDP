package com.timteam.symbidrive.symbidrive;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by zombie on 3/24/15.
 */
public class PassengerFragment extends Fragment implements View.OnClickListener{

    public PassengerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_passenger, container, false);
        initInterfaceElements(rootView);
        return rootView;
    }

    public void initInterfaceElements(View rootView){
        Button findPools = (Button)rootView.findViewById(R.id.btn_findPools);
        findPools.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getResources().
                getInteger(R.integer.passenger_section_id));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_findPools){
            Fragment matchingPoolsFragment = new MatchingPoolsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, matchingPoolsFragment)
                    .commit();
        }
    }
}
