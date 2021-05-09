package com.fatguy.behealthy;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainFragment extends Fragment {

    private View root;
    private ImageView Diagnose;
    private ImageView Tracker;
    private final FragmentManager fmg ;

    public MainFragment(FragmentManager fragmentManager) {
        fmg = fragmentManager;
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
                //render_tracker();
                startActivity(new Intent(getContext(), StepActivity.class));
            }
        });
        return root;
    }

    public View getRoot() {
        return root;
    }
    protected void render_diagnose()
    {
        DiagnoseFragment mainfrag = new DiagnoseFragment(getContext());
        TopBarDiagFragment topfrag = new TopBarDiagFragment();
        fmg.beginTransaction().replace(R.id.layoutMain,mainfrag,mainfrag.getTag()).commit();
        fmg.beginTransaction().replace(R.id.layout_top_nav,topfrag,topfrag.getTag()).commit();
    }
    protected  void render_tracker() {
        TrackerFragment trackerFragment = new TrackerFragment();
        TopBarMainFragment topBarMainFragment = new TopBarMainFragment();
        fmg.beginTransaction().replace(R.id.layoutMain,trackerFragment,trackerFragment.getTag()).commit();
        fmg.beginTransaction().replace(R.id.layout_top_nav,topBarMainFragment,topBarMainFragment.getTag()).commit();
    }
}