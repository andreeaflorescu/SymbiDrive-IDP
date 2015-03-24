package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.timteam.symbidrive.symbidrive.adapters.PoolsArrayAdapter;
import com.timteam.symbidrive.symbidrive.R;

/**
 * Created by zombie on 3/22/15.
 */
public class PoolsFragment extends Fragment {

    public PoolsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_navigation_pools, container, false);
        initInterfaceElements(rootView);
        return rootView;
    }

    public void initInterfaceElements(View rootView){

        ListView createdPools = (ListView)rootView.findViewById(R.id.createdPoolsList);
        ListView joinedPools = (ListView)rootView.findViewById(R.id.joinedPoolsList);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        PoolsArrayAdapter adapter = new PoolsArrayAdapter(getActivity().getApplicationContext(),
                values);

        createdPools.setAdapter(adapter);
        joinedPools.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getResources().
                getInteger(R.integer.pools_section_id));
    }
}
