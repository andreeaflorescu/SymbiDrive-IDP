package com.timteam.symbidrive.symbidrive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.timteam.symbidrive.symbidrive.R;
import com.timteam.symbidrive.symbidrive.fragments.MatchingPoolsFragment;
import com.timteam.symbidrive.symbidrive.helpers.PoolInfo;

import java.util.ArrayList;

public class MatchingPoolsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ArrayList<PoolInfo> pools = (ArrayList<PoolInfo>)intent.getSerializableExtra("pools");

        setContentView(R.layout.activity_matching_pools);

        MatchingPoolsFragment matchingPools = new MatchingPoolsFragment();
        matchingPools.setPools(pools);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_find_pool, matchingPools)
                    .commit();
        }
    }

}
