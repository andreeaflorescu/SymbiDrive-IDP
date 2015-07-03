package com.timteam.symbidrive.symbidrive.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timteam.symbidrive.symbidrive.R;

import java.util.Calendar;

public class FindScheduleLeaveFragment extends Fragment {

    public FindScheduleLeaveFragment() {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_find_schedule_leave, container, false);
        setTimePickerView(rootView);
        setDatePickerView(rootView);
        return rootView;
    }
}
