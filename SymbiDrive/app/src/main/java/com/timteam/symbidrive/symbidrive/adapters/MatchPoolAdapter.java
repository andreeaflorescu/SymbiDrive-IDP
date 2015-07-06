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
import android.widget.Toast;

import com.appspot.symbidrive_997.symbidrive.Symbidrive;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveManagePassangerRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidrivePoolResponse;

import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoRequest;
import com.appspot.symbidrive_997.symbidrive.model.SymbidriveUserInfoResponse;
import com.google.api.client.util.DateTime;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.activities.DriverProfileActivity;
import com.timteam.symbidrive.symbidrive.activities.MainActivity;
import com.timteam.symbidrive.symbidrive.helpers.AppConstants;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;
import com.timteam.symbidrive.symbidrive.helpers.SocialNetworkManager;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by zombie on 3/24/15.
 */
public class MatchPoolAdapter extends ArrayAdapter<PoolInfo> {

    private ArrayList<PoolInfo> pools;
    private Context context;
    private View rootView;
    private TextView tv_username;
    private boolean setOnce;

    public MatchPoolAdapter(Context context, int textViewResourceId, ArrayList<PoolInfo> objects) {
        super(context, textViewResourceId, objects);
        this.pools = objects;
        this.setOnce = true;
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
        this.rootView = rootView;

        tv_username = (TextView)rootView.findViewById(R.id.tv_driver_id);

        final PoolInfo poolInfo = pools.get(position);
        if(poolInfo != null){

            if(setOnce){
                getUserInfoTask(poolInfo.getDriverID());
                setOnce = false;
            }

            tv_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DriverProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("socialID", poolInfo.getDriverID());
                    context.startActivity(intent);
                }
            });

            try {
                updateView(rootView, poolInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void updateView(View rootView, final PoolInfo poolInfo) throws IOException {

        TextView source = (TextView)rootView.findViewById(R.id.tv_source_addr);
        String sourceAddress = DataManager.getAddress(poolInfo.getSourcePointLat(),
                poolInfo.getSourcePointLon(),
                context);
        source.setText(sourceAddress);

        TextView destination = (TextView)rootView.findViewById(R.id.tv_destination_addr);
        String destinationAddress = DataManager.getAddress(poolInfo.getDestinationPointLat(),
                poolInfo.getSourcePointLon(),
                context);
        destination.setText(destinationAddress);

        TextView seats = (TextView)rootView.findViewById(R.id.tv_seats);
        seats.setText("Seats: " + poolInfo.getSeats());

        TextView similatiry = (TextView)rootView.findViewById(R.id.tv_similatiry);
        similatiry.setText("0.666%");

        TextView tv_date = (TextView)rootView.findViewById(R.id.tv_date);
        TextView tv_time = (TextView)rootView.findViewById(R.id.tv_time);


        Calendar testC = Calendar.getInstance();
        testC.setTimeZone(TimeZone.getTimeZone("UTC"));
        testC.setTimeInMillis(poolInfo.getDate().getValue());

        String day="", month="", hour="", minute="";
        Integer i_day, i_month, i_hour, i_minute;

        i_day = testC.get(Calendar.DAY_OF_MONTH);
        if (i_day < 10) {
            day = "0" + i_day;
        }

        i_month = testC.get(Calendar.MONTH);
        if (i_month < 10) {
            month = "0" + i_month;
        }

        i_hour = testC.get(Calendar.HOUR);
        if (i_hour < 10) {
            hour = "0" + i_hour;
            Log.v("DATAAA", hour);
        }

        i_minute = testC.get(Calendar.MINUTE);
        if (i_minute < 10) {
            minute = "0" + i_minute;
            Log.v("DATAAA", minute);
        }

        String date = day + "/" + month + "/" + testC.get(Calendar.YEAR);
        String time = hour + " : " + minute;

        tv_date.setText(date);
        tv_time.setText(testC.get(Calendar.HOUR) + ":" + testC.get(Calendar.MINUTE));

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

    void getUserInfoTask(final String username){

        AsyncTask<Void, Void, SymbidriveUserInfoResponse> getUserInfoAsyncTask =
                new AsyncTask<Void, Void, SymbidriveUserInfoResponse>() {

                    @Override
                    protected SymbidriveUserInfoResponse doInBackground(Void... params) {

                        Symbidrive apiServiceHandle = AppConstants.getApiServiceHandle();

                        try {

                            SymbidriveUserInfoRequest getUserInfoRequest
                                    = new SymbidriveUserInfoRequest();
                            getUserInfoRequest.setSocialID(username);


                            return apiServiceHandle.getUserInfo(getUserInfoRequest).execute();

                        } catch (IOException e) {
                            Log.e("symbi", "Exception during API call", e);
                            //showMessage(e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(SymbidriveUserInfoResponse response) {
                        super.onPostExecute(response);
                        if (response != null) {
//                            Log.v("---------------on post execute ", response.getUsername());
                            setUsername(response.getUsername());
                        } else {
                            showMessage(context.getResources().getString(R.string.server_error_message));
                        }
                    }
                };
        getUserInfoAsyncTask.execute();
    }

    private void setUsername(String username) {
//        Log.v("-------------------------------set user name ", username);
        tv_username.setText(username);
    }

    private void showMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
