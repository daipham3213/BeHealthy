package com.fatguy.behealthy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;


public class ActivityGmap extends AppCompatActivity {


    private ConstraintLayout fragMap;
    private FragmentManager manager;
    private final String TAG = "ActivityGmap";
    static  public String name ;
    static public String address ;
    static public Double rate;
    static public String Status;
    static Double lng;
    static  Double lat;
    TextView Name;
    TextView Address;
    TextView RaTe;
    TextView STatus;

    public ActivityGmap(){}

    public  ActivityGmap(String name, String address, Double rate,String Status,Double lat, Double lng){
        this.name = name;
        this.address = address;
        this.rate = rate;
        this.Status = Status;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);
        //fragMap = findViewById(R.id.map_fragMap);
        MapsFragment map = new MapsFragment(name,address,rate,Status,lat,lng);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.map_fragMap,map,map.getTag()).addToBackStack(TAG).commit();
    }
}

