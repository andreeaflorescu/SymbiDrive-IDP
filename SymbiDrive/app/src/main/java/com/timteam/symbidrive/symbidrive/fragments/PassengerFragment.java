package com.timteam.symbidrive.symbidrive.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.MatchingPoolsFragment;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by zombie on 3/24/15.
 */
public class PassengerFragment extends Fragment implements View.OnClickListener{

    public PassengerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_passenger, container, false);
        initInterfaceElements(rootView);
        return rootView;
    }

    private void setTimePickerView(View rootView) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TextView timePicker = (TextView)rootView.findViewById(R.id.TimePicker);
        timePicker.setText(hour + ":" + minute);
    }

    private void setDatePickerView(View rootView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        TextView datePicker = (TextView)rootView.findViewById(R.id.DatePicker);
        datePicker.setText(day + "/" + month + "/" + year);
    }

    private void initInterfaceElements(View rootView) {
        setTimePickerView(rootView);
        setDatePickerView(rootView);
        Button findPools = (Button)rootView.findViewById(R.id.btn_findPools);
        findPools.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_findPools){
            Fragment matchingPoolsFragment = new MatchingPoolsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, matchingPoolsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
