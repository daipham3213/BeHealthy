package com.fatguy.behealthy;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class ActivityGmap extends Activity {

    private ConstraintLayout fragMap;
    private FragmentManager manager = getFragmentManager();
    private final String TAG = "ActivityGmap";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);
        fragMap = findViewById(R.id.map_fragMap);
        MapsFragment mapsFragment = new MapsFragment();


    }
}

