package com.timteam.symbidrive.symbidrive.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timteam.symbidrive.symbidrive.R;

import java.util.Calendar;

/**
 * Created by zombie on 3/24/15.
 */
public class PassengerFragment extends Fragment{

    public PassengerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_pool, container, false);
        initInterfaceElements(rootView);
        return rootView;
    }

    private void setTimePickerView(View rootView) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TextView timePicker = (TextView)rootView.findViewById(R.id.tv_time_picker);
        timePicker.setText(hour + ":" + minute);
    }

    private void setDatePickerView(View rootView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        TextView datePicker = (TextView)rootView.findViewById(R.id.tv_date_picker);
        datePicker.setText(day + "/" + month + "/" + year);
    }

    private void initInterfaceElements(View rootView) {
        setTimePickerView(rootView);
        setDatePickerView(rootView);
    }

}
