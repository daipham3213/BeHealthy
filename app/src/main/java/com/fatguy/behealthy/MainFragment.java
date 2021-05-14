package com.fatguy.behealthy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class MainFragment extends Fragment {

    private View root;
    private ImageView Diagnose;
    private ImageView Tracker;
    private ImageView Reminder;
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
        return root;
    }

    private void render_reminder() {
        startActivity(new Intent(getActivity(),ReminderActivity.class));
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
        startActivity(new Intent(getActivity(),TrackerActivity.class));
    }
}