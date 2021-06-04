package com.fatguy.behealthy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Adapters.HospitalAdapter;
import com.fatguy.behealthy.Models.getMap;
import com.fatguy.behealthy.Models.gmap.JSONgmap;
import com.fatguy.behealthy.Models.gmap.plus_code;
import com.fatguy.behealthy.Models.gmap.results;
import com.fatguy.behealthy.R;

import java.util.ArrayList;

public class HospitalActivity extends Activity {
    double longitude;
    double latitude;
    final String TAG = "HospitalActivity";
    private RecyclerView hospitals;
    private final ArrayList<String> name = new ArrayList<>();
    private final ArrayList<String> address = new ArrayList<>();
    private final ArrayList<String> status = new ArrayList<>();
    private final ArrayList<Double> rate = new ArrayList<>();
    private final ArrayList<Double> lat = new ArrayList<>();
    private final ArrayList<Double> lng = new ArrayList<>();
    JSONgmap jsonMap = new JSONgmap();
    getMap data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospital);
        hospitals = findViewById(R.id.hospital_rclView);
        Log.d(TAG, "hospitalListInit: getLocation");
        data = new getMap(this, longitude, latitude, 1000, "hospital");
        hospitalListInit();


    }

    private void hospitalListInit() throws NullPointerException {
        Intent intent = getIntent();
        jsonMap = (JSONgmap) intent.getSerializableExtra("gmap");
        Log.d(TAG, "initData: Done" + jsonMap);
        if (jsonMap != null)
            if (jsonMap.getStatus() != null) {
                Log.d(TAG, "hospitalListInit: translateData");
                int count = jsonMap.counter();
                for (int i = 0; i < count - 1; i++) {
                    Log.d(TAG, "onCreate: hospital list +1");
                    results rs = jsonMap.getResults()[i];
                    plus_code pl = rs.getPlus_code();
                    name.add(rs.getName());
                    address.add(rs.getVicinity() + pl.getCompound_code().substring(pl.getCompound_code().indexOf(" ")));
                    status.add(rs.getBusiness_status());
                    rate.add(rs.getRating());
                    lat.add(rs.getGeometry().getLocation().getLat());
                    lng.add(rs.getGeometry().getLocation().getLng());
                }
            }
        Log.d(TAG, "hospitalListInit: initAdapter");
        HospitalAdapter adapter = new HospitalAdapter(this, name, address, status, rate, lat, lng);
        hospitals.setAdapter(adapter);
        hospitals.setLayoutManager(new LinearLayoutManager(this));
    }



}
