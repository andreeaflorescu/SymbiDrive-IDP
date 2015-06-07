package com.timteam.symbidrive.symbidrive.adapters;

import android.content.Context;
import android.support.v4.util.Pools;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.bustling_bay_88919.symbidrive.model.SymbidriveManagePassangerRequest;
import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;

import java.util.ArrayList;

/**
 * Created by zombie on 3/24/15.
 */
public class MatchPoolAdapter extends ArrayAdapter<PoolInfo> {

    private ArrayList<PoolInfo> pools;

    public MatchPoolAdapter(Context context, int textViewResourceId, ArrayList<PoolInfo> objects) {
        super(context, textViewResourceId, objects);
        this.pools = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.match_pool_adapter, parent, false);

        PoolInfo poolInfo = pools.get(position);
        if(poolInfo != null){
            TextView driverID = (TextView)rootView.findViewById(R.id.tv_driver_id);
            driverID.setText(poolInfo.getDriverID());

            Button joinPool = (Button)rootView.findViewById(R.id.btn_join_pool);
            joinPool.setTag(poolInfo.getPoolID());

            joinPool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("pool id", v.getTag() + "");
                    //TODO
                    //SymbidriveManagePassangerRequest
                }
            });
        }

        return rootView;
    }
}
