package com.fatguy.behealthy.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.fatguy.behealthy.Activities.HeartRateMonitor;
import com.fatguy.behealthy.Activities.HospitalActivity;
import com.fatguy.behealthy.Activities.ReminderActivity;
import com.fatguy.behealthy.Activities.TrackerActivity;
import com.fatguy.behealthy.R;


public class MainFragment extends Fragment {

    private View root;
    private ImageView Diagnose;
    private ImageView Tracker;
    private ImageView Reminder;
    private ImageView Hospital;
    private ImageView Heart;

    private final String TAG ="MainActivity";

    public MainFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_main, container, false);
        Diagnose = root.findViewById(R.id.main_btnDiagnose);
        Diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                render_diagnose();
            }
        });
        Tracker = root.findViewById(R.id.btnTracker);
        Tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                render_tracker();
            }
        });
        Reminder = root.findViewById(R.id.btnReminder);
        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                render_reminder();
            }
        });
        Hospital = root.findViewById(R.id.btnAmblance);
        Hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                render_hosptal();
            }
        });
        Heart = root.findViewById(R.id.btnHeartrate);
        Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                render_heartmonitor();
            }
        });

        return root;
    }

    private void render_hosptal() {
        startActivity(new Intent(getActivity(), HospitalActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    private void render_heartmonitor() {
        startActivity(new Intent(getActivity(), HeartRateMonitor.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void render_reminder() {
        startActivity(new Intent(getActivity(), ReminderActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public View getRoot() {
        return root;
    }
    protected void render_diagnose()
    {
        DiagnoseFragment mainFrag = new DiagnoseFragment(getContext());
        TopBarDiagFragment topFrag = new TopBarDiagFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.layoutMain,mainFrag,mainFrag.getTag()).addToBackStack(TAG).commit();
        getParentFragmentManager().beginTransaction().replace(R.id.layout_top_nav,topFrag,topFrag.getTag()).commit();
    }
    protected  void render_tracker() {
        startActivity(new Intent(getActivity(), TrackerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}