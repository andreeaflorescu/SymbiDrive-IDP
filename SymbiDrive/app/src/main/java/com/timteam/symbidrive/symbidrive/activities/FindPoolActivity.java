package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.DatePickerFragment;
import com.timteam.symbidrive.symbidrive.fragments.PassengerFragment;
import com.timteam.symbidrive.symbidrive.fragments.TimePickerFragment;


public class FindPoolActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pool);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_find_pool, new PassengerFragment())
                    .commit();
        }
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment;
        newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void findMatchingPools(View view) {
        Intent intent = new Intent(this, MatchingPoolsActivity.class);
        startActivity(intent);
    }
}