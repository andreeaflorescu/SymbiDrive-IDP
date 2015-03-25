package com.timteam.symbidrive.symbidrive.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.timteam.symbidrive.symbidrive.R;

/**
 * Created by zombie on 3/24/15.
 */
public class MatchPoolAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public MatchPoolAdapter(Context context, String[] values) {

        super(context, R.layout.matching_pools, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.match_pool_adapter, parent, false);

        return rowView;
    }
}
