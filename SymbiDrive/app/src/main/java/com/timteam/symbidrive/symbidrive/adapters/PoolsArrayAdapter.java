package com.timteam.symbidrive.symbidrive.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.helpers.DataManager;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zombie on 3/24/15.
 */
public class PoolsArrayAdapter extends ArrayAdapter<PoolInfo> {

    private final Context context;
    private final PoolInfo[] poolInfos;

    public PoolsArrayAdapter(Context context, PoolInfo[] values) {

        super(context, R.layout.fragment_navigation_pools, values);
        this.context = context;
        this.poolInfos = values;
    }

    public PoolInfo getPoolInfo(int position) {
        return poolInfos[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pool_list_adapter, parent, false);

        String sourceAddress = "", destinationAddress = "";
        try {
            sourceAddress = DataManager.getAddress(poolInfos[position].getSourcePointLat(),
                                                        poolInfos[position].getSourcePointLon(), getContext());
            destinationAddress = DataManager.getAddress(poolInfos[position].getDestinationPointLat(),
                    poolInfos[position].getDestinationPointLon(), getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
            Calendar testC = Calendar.getInstance();
            testC.setTimeZone(TimeZone.getTimeZone("UTC"));
            testC.setTimeInMillis(poolInfos[position].getDate().getValue());
//            Log.v("DATA", testC.get(Calendar.HOUR) + ":" + testC.get(Calendar.MINUTE));



            String day="", month="", hour="", minute="";
            Integer i_day, i_month, i_hour, i_minute;
//
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
            Log.v("DATAA", date + "   " + time);
            ((TextView) rowView.findViewById(R.id.tv_date)).setText(date);
            ((TextView) rowView.findViewById(R.id.tv_time)).setText( testC.get(Calendar.HOUR) + ":" + testC.get(Calendar.MINUTE));


        ((TextView) rowView.findViewById(R.id.tv_source_addr)).setText(sourceAddress);
        ((TextView)rowView.findViewById(R.id.tv_destination_addr)).setText(destinationAddress);
        return rowView;
    }
}
