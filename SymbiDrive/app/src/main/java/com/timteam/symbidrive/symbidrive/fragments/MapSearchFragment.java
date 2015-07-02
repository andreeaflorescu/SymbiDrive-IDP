package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.adapters.PlacesAutoCompleteAdapter;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;

import java.io.IOException;
import java.util.Calendar;

public class MapSearchFragment extends Fragment {
    private View mRootView;
    private boolean isDestinationPointSet;
    private boolean isSourcePointSet;
    private GoogleMap map;
    private AutoCompleteTextView sourceLocation;
    private AutoCompleteTextView destinationLocation;
    private SupportMapFragment fragment;

    private Marker sourceMarker;
    private Marker destinationMarker;

    public MapSearchFragment() {
        // Required empty public constructor
        isDestinationPointSet = false;
        isSourcePointSet = false;
    }

    public LatLng getSourceLocation() {

        if (!isSourcePointSet) {
            return null;
        }

        return sourceMarker.getPosition();
    }

    public LatLng getDestinationLocation() {
        if (!isDestinationPointSet) {
            return null;
        }

        return destinationMarker.getPosition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_search, container, false);
        initInterfaceElements(rootView);
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

    private void addMarker(String marker_type, LatLng location) {
        switch (marker_type) {
            case "source": {
                // check if marker is already set, in which case delete it
                if (isSourcePointSet) {
                    sourceMarker.remove();
                }
                isSourcePointSet = true;

                sourceMarker = map.addMarker(new MarkerOptions()
                        .position(location)
                        .title("Source")
                        .draggable(true));
                break;
            }
            case "destination": {
                // check if marker is already set, in which case delete it
                if (isDestinationPointSet) {
                    destinationMarker.remove();
                }
                isDestinationPointSet = true;

                destinationMarker = map.addMarker(new MarkerOptions()
                        .position(location)
                        .title("Destination")
                        .draggable(true));
                break;
            }
            default:
                break;
        }

        // zoom into the location of marker
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            sourceLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        double[] coord = DataManager.getCoordinates(((TextView) view).getText().toString(), view.getContext());
                        LatLng location = new LatLng(coord[0], coord[1]);
                        addMarker("source", location);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            destinationLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        double[] coord = DataManager.getCoordinates(((TextView) view).getText().toString(), view.getContext());
                        LatLng location = new LatLng(coord[0], coord[1]);
                        addMarker("destination", location);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }


    private void initInterfaceElements(View rootView) {


        sourceLocation = (AutoCompleteTextView) rootView.
                findViewById(R.id.et_source_location);
        if (sourceLocation != null) {
            sourceLocation.setAdapter(new PlacesAutoCompleteAdapter(getActivity(),
                    R.layout.autocomplete_list_view));
        }

        destinationLocation = (AutoCompleteTextView) rootView.
                findViewById(R.id.et_destination_location);
        if (destinationLocation != null) {
            destinationLocation.setAdapter(new PlacesAutoCompleteAdapter(getActivity(),
                    R.layout.autocomplete_list_view));
        }
    }
}
