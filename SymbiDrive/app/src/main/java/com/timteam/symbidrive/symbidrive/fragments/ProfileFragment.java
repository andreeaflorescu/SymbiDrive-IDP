package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.timteam.symbidrive.symbidrive.R;

/**
 * Created by zombie on 3/22/15.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_profile, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getResources().
                getInteger(R.integer.profile_section_id));
    }

}
