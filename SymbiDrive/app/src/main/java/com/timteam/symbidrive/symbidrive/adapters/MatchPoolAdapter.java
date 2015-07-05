package com.timteam.symbidrive.symbidrive.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveManagePassangerRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidrivePoolResponse;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zombie on 3/24/15.
 */
public class MatchPoolAdapter extends ArrayAdapter<PoolInfo> {

    private ArrayList<PoolInfo> pools;
    private Context context;

    public MatchPoolAdapter(Context context, int textViewResourceId, ArrayList<PoolInfo> objects) {
        super(context, textViewResourceId, objects);
        this.pools = objects;
        this.context = context;
    }

    public void openPoolsActivity(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("section", 1);
        context.startActivity(intent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.match_pool_adapter, parent, false);

        PoolInfo poolInfo = pools.get(position);
        if(poolInfo != null){

            TextView driverID = (TextView)rootView.findViewById(R.id.tv_driver_id);
            driverID.setText("username");

            final Button joinPool = (Button)rootView.findViewById(R.id.btn_join_pool);
            joinPool.setTag(poolInfo.getPoolID());

            joinPool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("pool id", v.getTag() + "");
                    //TODO
                    //SymbidriveManagePassangerRequest
                    joinPool(Long.parseLong(v.getTag() + ""),
                            SocialNetworkManager.getInstance().getSocialTokenID());
                }
            });
        }

        return rootView;
    }

    private void joinPool(final long poolID, final String passengerID) {

        AsyncTask<Void, Void, SymbidrivePoolResponse> joinPoolTask =
                new AsyncTask<Void, Void, SymbidrivePoolResponse>() {

            @Override
            protected SymbidrivePoolResponse doInBackground(Void... params) {

                try {

                    Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                    SymbidriveManagePassangerRequest joinPoolRequest =
                            new SymbidriveManagePassangerRequest();
                    joinPoolRequest.setPassengerId(passengerID);
                    joinPoolRequest.setPoolId(poolID);

                    return apiServiceHandle.addPassengerToPool(joinPoolRequest).execute();

                }
                catch (IOException e){
                    Log.e("symbi", "Exception during API call", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(SymbidrivePoolResponse response){
                if (response != null) {
                    Log.v("symbi", response.toString());
//                    if (response.getRet() == "PASSENGER_ADDED_TO_POOL") {
                        openPoolsActivity();
//                    } else {
                        // TODO display warning message as dialog for pool not found
//                    }

                } else {
                    Log.v("symbi", "No greetings were returned by the API.");
                }
            }
        };
        joinPoolTask.execute((Void) null);
    }

}
