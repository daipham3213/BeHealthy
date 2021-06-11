package com.fatguy.behealthy.Activities;


import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.fatguy.behealthy.Models.Utils;
import com.fatguy.behealthy.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class TrackerActivity extends Activity implements SensorEventListener {
    private IconRoundCornerProgressBar step_chart;
    private FirebaseDatabase mData;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private SensorManager sensorManager = null;
    private long total_step = 0;
    private long prev_step = 0;
    final long[] target = new long[1];
    final long[] counted = new long[1];
    final ArrayList<BarEntry> weekly_data = new ArrayList<>();
    final ArrayList<BarEntry> date_data = new ArrayList<>();
    private Sensor stepSensor;
    boolean running = false;
    private TextView number;
    private TextView percent;
    String d2s;
    private BarChart week_chart;
    private static final String TAG = "TrackerActivity";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get today
        setContentView(R.layout.activity_tracker);
        d2s = Utils.dateFormat(0);
        //init var
        sensorManager  = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference().child("StepCounter").child(mAuth.getUid()); //link to Step table of current user of today
        step_chart = findViewById(R.id.tracker_chart);
        number =findViewById(R.id.tracker_txtProgess);
        percent =findViewById(R.id.tracker_txtPercent);
        week_chart = findViewById(R.id.tracker_weekly_chart);
        loadData();
        reset_chart();
        loadWeeklyData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            Toast.makeText(this, "Sensor not detected!", Toast.LENGTH_SHORT).show();
        } else
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
            total_step = (long) event.values[0];
            long currStep = total_step - prev_step;
            saveData(currStep);
            progress(target[0], currStep);
            Log.d(TAG, "New step detected! - " + currStep);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void reset_chart() {
        step_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TrackerActivity.this, "Long click to reset", Toast.LENGTH_SHORT).show();
            }
        });
        step_chart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                prev_step = total_step;
                counted[0] = 0;
                progress(target[0], counted[0]);
                Log.d(TAG, "Reseted");
                saveData(0);
                return true;
            }
        });
    }

    private void saveData(long val) {
        SharedPreferences preferences = getSharedPreferences("stepCounter",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("stepsKey",prev_step);
        editor.apply();
        mRef.child(d2s).child("counted").setValue(val);
    }

    private void loadData(){
        SharedPreferences preferences = getSharedPreferences("stepCounter",MODE_PRIVATE);
        long prev = preferences.getLong("stepsKey",0);
        prev_step = prev;

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@org.jetbrains.annotations.NotNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild(d2s)) {
                    counted[0] = snapshot.child(d2s).child("counted").getValue(Long.TYPE);
                    if (snapshot.child(d2s).child("target").getValue() != null) {
                        target[0] = snapshot.child(d2s).child("target").getValue(Long.TYPE);
                    } else mRef.child(d2s).child("target").setValue(8000);
                    progress(target[0], counted[0]);
                } else {
                    mRef.child(d2s).child("counted").setValue(0);
                    mRef.child(d2s).child("target").setValue(8000);
                    Log.d(TAG, "New instance set!");
                    progress(8000, 0);
                    prev_step = total_step;
                    counted[0] = 0;
                    progress(8000, 0);
                    Log.d(TAG, "Reseted");
                    saveData(0);
                }
                Log.d(TAG, "Steps counted on DB: "+counted[0]);
            }

            @Override
            public void onCancelled(@org.jetbrains.annotations.NotNull @NotNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadWeeklyData(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                LocalDate date = LocalDate.now().minusDays(7);
                for (int i = 0; i<7; i++) {
                    String st_date = date.format(formatter);
                    int finalI = i;
                    if (snapshot.hasChild(st_date) && snapshot.child(st_date).child("target").getValue(Long.TYPE) != null) {
                        long val = snapshot.child(st_date).child("counted").getValue(Long.TYPE);
                        weekly_data.add(new BarEntry(i, val));
                        date_data.add(new BarEntry(i,date.getDayOfMonth()));
                        //Log.d(TAG, "onDataChange: " + val + " -" + st_date);
                    }
                    date = date.plusDays(1);
                }
                BarDataSet bardataset = new BarDataSet(weekly_data, "Daily record");
                BarDataSet days = new BarDataSet(date_data, "Date");
                bardataset.setColors(ColorTemplate.LIBERTY_COLORS);
                BarData data = new BarData(days,bardataset);

                week_chart.setData(data);
                week_chart.setDrawBarShadow(true);
                Description desc = new Description();
                desc.setText("Weekly counted step");
                week_chart.setDescription(desc);
                week_chart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

    }

    private void progress(long target, long counted){
        if (target == 0) target = 1; //divide by 0
        float percent =( (float)counted/target)*100;
        this.percent.setText((int) percent + "%");
        number.setText((total_step - prev_step) + " / " + target);
        step_chart.setProgress(percent);
    }

}
