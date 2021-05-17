package com.fatguy.behealthy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.gmap.JSONgmap;
import com.fatguy.behealthy.gmap.plus_code;
import com.fatguy.behealthy.gmap.results;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HospitalActivity extends Activity {

    Location gps_loc;
    Location network_loc;
    Location final_loc;
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
    getData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospital);

        hospitals = findViewById(R.id.hospital_rclView);
        try {
            getCurrLocation();
            Log.d(TAG, "hospitalListInit: getLocation");
            data = new getData(this, longitude, latitude, 1000, "hospital");
            hospitalListInit();
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void hospitalListInit() throws JSONException, InterruptedException, ExecutionException {
        jsonMap = data.getMap();
        if (jsonMap != null)
            if (jsonMap.getStatus() != null) {
                Log.d(TAG, "hospitalListInit: translateData");
                int count = jsonMap.counter();
                for (int i = 0; i < count; i++) {
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

    private void getCurrLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACTIVITY_RECOGNITION}, 1);
    }

}
