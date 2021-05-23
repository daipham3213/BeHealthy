package com.fatguy.behealthy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    public static String name ;
    public static String address ;
    public static Double rate;
    public static String Status;
    Double lng;
    Double lat;
    TextView Name;
    TextView Address;
    TextView RaTe;
    TextView txtStatus;
    View root;

    public MapsFragment (String name, String address, Double rate, String status, Double lat, Double lng){
        this.name = name;
        this.address = address;
        this.Status = status;
        this.rate = rate;
        this.lat = lat;
        this.lng = lng;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_gmap, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragMap);
        mapFragment.getMapAsync(this);

        Name = root.findViewById(R.id.frag_map_name2);
        Address = root.findViewById(R.id.frag_map_address);
        RaTe = root.findViewById(R.id.frag_map_rate);
        txtStatus = root.findViewById(R.id.frag_map_business_status);

        Name.setText(this.name);
        Address.setText(this.address);
        RaTe.setText(Double.valueOf(this.rate).toString());
        txtStatus.setText(this.Status);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng MLatLng = new LatLng(lat,lng);
        googleMap.addMarker(new MarkerOptions().position(MLatLng).title(this.name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(MLatLng));
    }
}