package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;

import java.util.ArrayList;

/**
 * Created by zombie on 7/4/15.
 */
public class DriverProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_profile);
        Intent intent = getIntent();
        String socialID = intent.getStringExtra("socialID");

    }
}
