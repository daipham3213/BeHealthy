package com.fatguy.behealthy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class AcitivityGMap extends AppCompatActivity {

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
    TextView status;
    ConstraintLayout Map;

    public AcitivityGMap(){}

    public AcitivityGMap(String name, String address, Double rate, String Status, Double lat, Double lng){
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
        MapsFragment map = new MapsFragment(lat,lng);
        manager = getSupportFragmentManager();

        Name = findViewById(R.id.frag_map_name2);
        Address = findViewById(R.id.frag_map_address);
        RaTe = findViewById(R.id.frag_map_rate);
        status = findViewById(R.id.frag_map_business_status);
        Map = findViewById(R.id.hospital_click_2);

        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.map_fragMap,map,map.getTag()).commit();
            }
        });

        Name.setText(this.name);
        Address.setText(this.address);
        RaTe.setText(Double.valueOf(this.rate).toString());
        status.setText(this.Status);

        manager.beginTransaction().replace(R.id.map_fragMap,map,map.getTag()).commit();
    }
}

