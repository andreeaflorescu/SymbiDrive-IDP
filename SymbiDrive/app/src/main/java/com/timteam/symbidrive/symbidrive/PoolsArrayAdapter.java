package com.timteam.symbidrive.symbidrive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by zombie on 3/24/15.
 */
public class PoolsArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public PoolsArrayAdapter(Context context, String[] values) {

        super(context, R.layout.fragment_navigation_pools, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pool_list_adapter, parent, false);

        TextView textView = (TextView)rowView.findViewById(R.id.adapterItemText);
        textView.setText(values[position]);

        return rowView;
    }
}
