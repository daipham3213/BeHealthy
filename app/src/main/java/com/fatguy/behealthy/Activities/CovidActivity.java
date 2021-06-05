package com.fatguy.behealthy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fatguy.behealthy.Models.Covid.Covid19;
import com.fatguy.behealthy.R;

public class CovidActivity extends AppCompatActivity {
    private static final String TAG = "CovidActivity";
    private Covid19 data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);
        initData();
    }

    private void initData() {
        Intent i = getIntent();
        data = (Covid19) i.getSerializableExtra("data");
        Log.d(TAG, "initData: Done" + data);
    }
}