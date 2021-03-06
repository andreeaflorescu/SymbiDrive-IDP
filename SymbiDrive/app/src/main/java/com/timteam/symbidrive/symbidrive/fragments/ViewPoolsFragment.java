package com.timteam.symbidrive.symbidrive.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveCreatePoolRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidrivePoolResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveSinglePoolResponse;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveViewPoolsRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveViewPoolsResponse;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.CreatedPoolDetailsActivity;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.timteam.symbidrive.symbidrive.adapters.PoolsArrayAdapter;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zombie on 3/22/15.
 */
public class ViewPoolsFragment extends Fragment {

    public ViewPoolsFragment(){

    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_navigation_pools, container, false);
        this.rootView = rootView;
        postGetPools();
        return rootView;
    }

    public void initInterfaceElements(List<SymbidriveSinglePoolResponse> createdPoolsResponse,
                                      List<SymbidriveSinglePoolResponse> joinedPoolsResponse){

        ListView createdPoolsListView = (ListView)rootView.findViewById(R.id.createdPoolsList);
        ListView joinedPoolsListView = (ListView)rootView.findViewById(R.id.joinedPoolsList);
        PoolInfo[] createdPoolInfos;
        PoolInfo[] joinedPoolInfo;



        if (createdPoolsResponse == null) {
            createdPoolInfos = new PoolInfo[0];
        } else {
            createdPoolInfos = DataManager.getPoolInfoFromPoolResponse(createdPoolsResponse);

            View flayout = rootView.findViewById(R.id.tv_no_created_pools);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) flayout.getLayoutParams();
            lp.height = 0;
            lp.width = 0;
            flayout.setLayoutParams(lp);
        }

        if (joinedPoolsResponse == null) {
            joinedPoolInfo = new PoolInfo[0];
        } else {
            joinedPoolInfo = DataManager.getPoolInfoFromPoolResponse(joinedPoolsResponse);

            createdPoolInfos = DataManager.getPoolInfoFromPoolResponse(createdPoolsResponse);
            View flayout = rootView.findViewById(R.id.tv_no_joined_pools);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) flayout.getLayoutParams();
            lp.height = 0;
            lp.width = 0;
            flayout.setLayoutParams(lp);
        }

        createdPoolsListView.setAdapter(new PoolsArrayAdapter(rootView.getContext(), createdPoolInfos));
        joinedPoolsListView.setAdapter(new PoolsArrayAdapter(rootView.getContext(), joinedPoolInfo));

        createdPoolsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoolsArrayAdapter adapter = (PoolsArrayAdapter) parent.getAdapter();
                openCreatedPoolDetails(adapter.getPoolInfo(position));
            }
        });

        joinedPoolsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoolsArrayAdapter adapter = (PoolsArrayAdapter) parent.getAdapter();
                openJoinedPoolDetails(adapter.getPoolInfo(position));
            }
        });


    }

    private void openCreatedPoolDetails(PoolInfo poolInfo) {
        Intent createPoolDetailsActivity = new Intent(this.getActivity(), CreatedPoolDetailsActivity.class);
        createPoolDetailsActivity.putExtra("type", "created");
        createPoolDetailsActivity.putExtra("destinationPointLat", poolInfo.getDestinationPointLat());
        createPoolDetailsActivity.putExtra("destinationPointLon", poolInfo.getDestinationPointLon());
        createPoolDetailsActivity.putExtra("sourcePointLat", poolInfo.getSourcePointLat());
        createPoolDetailsActivity.putExtra("sourcePointLon", poolInfo.getSourcePointLon());
        createPoolDetailsActivity.putExtra("poolID", poolInfo.getPoolID());
        createPoolDetailsActivity.putExtra("driverUserName", poolInfo.getDriverUserName());
        createPoolDetailsActivity.putExtra("driverID", poolInfo.getDriverID());
        createPoolDetailsActivity.putExtra("seats", poolInfo.getSeats());
        createPoolDetailsActivity.putExtra("date", poolInfo.getDate());
        String[] passengers;
        List<String> arr_passengers = poolInfo.getPassengers();
        if (arr_passengers == null) {
            passengers = null;
        } else {
            passengers = new String[arr_passengers.size()];
            passengers = poolInfo.getPassengers().toArray(passengers);
        }
        createPoolDetailsActivity.putExtra("passengersIDS", passengers);
        startActivity(createPoolDetailsActivity);
    }

    private void openJoinedPoolDetails(PoolInfo poolInfo) {
        Intent createPoolDetailsActivity = new Intent(this.getActivity(), CreatedPoolDetailsActivity.class);

        createPoolDetailsActivity.putExtra("type", "joined");
        createPoolDetailsActivity.putExtra("destinationPointLat", poolInfo.getDestinationPointLat());
        createPoolDetailsActivity.putExtra("destinationPointLon", poolInfo.getDestinationPointLon());
        createPoolDetailsActivity.putExtra("sourcePointLat", poolInfo.getSourcePointLat());
        createPoolDetailsActivity.putExtra("sourcePointLon", poolInfo.getSourcePointLon());
        createPoolDetailsActivity.putExtra("poolID", poolInfo.getPoolID());
        createPoolDetailsActivity.putExtra("driverUserName", poolInfo.getDriverUserName());
        createPoolDetailsActivity.putExtra("driverID", poolInfo.getDriverID());
        createPoolDetailsActivity.putExtra("date", poolInfo.getDate().toString());

        startActivity(createPoolDetailsActivity);
    }

    public void postGetPools() {
        AsyncTask<Void, Void, SymbidriveViewPoolsResponse> postPoolInfo =
                new AsyncTask<Void, Void, SymbidriveViewPoolsResponse>() {

                    @Override
                    protected SymbidriveViewPoolsResponse doInBackground(Void... integers) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveViewPoolsRequest getPoolsRequest =
                                    new SymbidriveViewPoolsRequest();
                            getPoolsRequest.setSocialID(SocialNetworkManager
                                    .getInstance()
                                    .getSocialTokenID());
                            return apiServiceHandle.viewPools(getPoolsRequest).execute();
                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveViewPoolsResponse response) {
                        if (response != null) {
                            Log.v("symbi", response.getCreatedPools().toString());
                            initInterfaceElements(response.getCreatedPools(), response.getJoinedPools());

                        } else {
                            Log.v("symbi", "No greetings were returned by the API.");
                        }
                    }
                };

        postPoolInfo.execute((Void) null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getResources().
                getInteger(R.integer.pools_section_id));
    }
}
