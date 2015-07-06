package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;

/**
 * Created by zombie on 3/22/15.
 */
public class RegisterRouteFragment extends Fragment {

    private View mRootView;
    private SupportMapFragment fragment;
    public RegisterRouteFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_navigation_register_route, container, false);
        this.mRootView = rootView;

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getResources().
                getInteger(R.integer.register_route_section_id));
    }

    @Override
    public void onDestroyView() {

        SupportMapFragment f = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map_view_pool);

        if (f != null) {
            try {
                getFragmentManager().beginTransaction().remove(f).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onDestroyView();
    }
}
