package com.fatguy.behealthy;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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


public class TrackerActivity extends Activity implements SensorEventListener {
    private PieChart step_chart;
    private FirebaseDatabase mData;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SensorManager sensorManager = null;
    private long total_step = 0;
    final long[] target = new long[1];
    final long[] counted = new long[1];
    private Sensor stepSensor;
    boolean running = false;
    String d2s;
    String TAG = "TrackerFragment";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get today
        setContentView(R.layout.activity_tracker);
        Date date = Calendar.getInstance().getTime();
        d2s = dateFormat.format(date);
        //init var
        sensorManager  = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference().child("Steps").child(mAuth.getUid()).child(d2s); //link to Step table of current user of today
        step_chart = findViewById(R.id.tracker_step_chart);
        loadData();
        reset_chart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null)
        {
            Toast.makeText(this, "Sensor not detected!", Toast.LENGTH_SHORT).show();
        } else
            sensorManager.registerListener(this,stepSensor,sensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
            total_step = (long) event.values[0];
            long currStep = total_step - counted[0];
            addValue_pie(step_chart,target[0],currStep);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void reset_chart()
    {
        step_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TrackerActivity.this, "Long click to reset", Toast.LENGTH_SHORT).show();
            }
        });
        step_chart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                counted[0] = total_step;
                addValue_pie(step_chart,target[0],0);
                saveData();
                return true;
            }
        });
    }

    private void saveData() {
        mRef.child("counted").setValue(total_step);
    }

    private void loadData(){

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild("counted"))
                {
                    counted[0] = (long) snapshot.child("counted").getValue();
                    target [0] = (long) snapshot.child("target").getValue();
                }
                else
                {
                    mRef.child("counted").setValue(0);
                    mRef.child("target").setValue(8000);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        Log.d(TAG, "loadData: "+counted[0]);

    }

    private void addValue_pie(PieChart step_chart, long target, long counted) {
        long left;
        if (target - counted < 0) left = 0;
        else left = target - counted;
        PieDataSet lds = new PieDataSet(step_values(left, counted), "");
        PieData step_data = new PieData(lds);


        int color[] = {R.color.md_teal_100, R.color.md_blue_grey_100};
        lds.setColors(color);
        //insert data
        step_chart.clear();
        String tg = String.valueOf(counted);
        step_chart.setCenterText(tg);
        step_chart.setData(step_data);
        //Customize pie chart
        step_chart.setCenterTextSize(20);
        step_chart.setDrawEntryLabels(false);
        step_chart.setHoleRadius(75);
        step_chart.setTransparentCircleRadius(80);
        step_chart.setUsePercentValues(true);
        step_chart.setDescription(null);
        step_chart.invalidate();

    }


    private ArrayList<PieEntry> step_values(long target, long counted) {
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(target, "Target"));
        data.add(new PieEntry(counted, "Counted"));
        return data;
    }



}
