package com.fatguy.behealthy;


import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class TrackerFragment extends Fragment {
    private View root;
    private PieChart step_chart;
    private FirebaseDatabase mData;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SensorManager sensorManager;
    boolean activityRunning;
    final long[] target = new long[1];
    final long[] counted = new long[1];

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tracker, container, false);
        mData = mData.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRef = mData.getReference().child("Step").child(mAuth.getUid());
        step_chart = root.findViewById(R.id.tracker_step_chart);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Date date = Calendar.getInstance().getTime();
                String d2s = dateFormat.format(date);
                if (snapshot.hasChild(d2s)) {
                    target[0] = (long) snapshot.child(d2s).child("target").getValue();
                    counted[0] = (long) snapshot.child(d2s).child("counted").getValue();
                } else {
                    mRef.child(d2s).child("target").setValue(8000);
                    mRef.child(d2s).child("counted").setValue(1);
                    target[0] = 8000;
                    counted[0] = 1;
                }
                addValue_pie(step_chart, target[0], counted[0]);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return root;
    }

    private void addValue_pie(PieChart step_chart, long target, long counted) {
        long left;
        if (target - counted < 0) left = 0;
        else left = target - counted;
        PieDataSet lds = new PieDataSet(step_values(left, counted), "");
        PieData step_data = new PieData(lds);

        //Customize pie chart
        int color[] = {R.color.md_teal_100, R.color.md_blue_grey_100};
        lds.setColors(color);
        String tg = String.valueOf(counted);
        step_chart.setCenterText(tg);
        step_chart.setCenterTextSize(20);
        step_chart.setDrawEntryLabels(false);
        step_chart.setData(step_data);
        step_chart.setHoleRadius(75);
        step_chart.setTransparentCircleRadius(80);
        step_chart.setUsePercentValues(true);
        step_chart.setDescription(null);
        step_chart.invalidate();
    }

    public TrackerFragment() {
    }


    private ArrayList<PieEntry> step_values(long target, long counted) {
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(target, "Target"));
        data.add(new PieEntry(counted, "Counted"));
        return data;
    }
}
