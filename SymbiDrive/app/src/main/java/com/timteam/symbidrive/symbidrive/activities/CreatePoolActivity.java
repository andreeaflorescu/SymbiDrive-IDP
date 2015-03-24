package com.timteam.symbidrive.symbidrive.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.DriverFragment;


public class CreatePoolActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_create_pool, new DriverFragment())
                    .commit();
        }
    }
}
