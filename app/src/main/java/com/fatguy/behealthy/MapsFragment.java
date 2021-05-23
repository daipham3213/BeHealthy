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
    View root;

    public MapsFragment (Double lat, Double lng){
        this.lat = lat;
        this.lng = lng;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment)  this.getChildFragmentManager()
                .findFragmentById(R.id.frag_gmap_cont);
        mapFragment.getMapAsync(this);

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
        googleMap.setMinZoomPreference(15.0f);
        googleMap.setMaxZoomPreference(34.0f);
    }
}