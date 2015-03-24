package com.timteam.symbidrive.symbidrive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by zombie on 3/24/15.
 */
public class MatchingPoolsFragment extends Fragment{

    public MatchingPoolsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.matching_pools, container, false);
        initInterfaceElements(rootView);
        return rootView;
    }

    private void initInterfaceElements(View rootView){

        String[] values = new String[] { "Android", "iPhone", "Android", "iPhone",
                "Android", "iPhone" };
        MatchPoolAdapter adapter = new MatchPoolAdapter(getActivity().getApplicationContext(),
                values);

        ListView matchingPools = (ListView)rootView.findViewById(R.id.matchingPoolsList);
        matchingPools.setAdapter(adapter);

    }

}
