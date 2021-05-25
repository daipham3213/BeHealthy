package com.fatguy.behealthy.Activities;


import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.fatguy.behealthy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TrackerActivity extends Activity implements SensorEventListener {
    private IconRoundCornerProgressBar step_chart;
    private FirebaseDatabase mData;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SensorManager sensorManager = null;
    private long total_step = 0;
    private long prev_step = 0;
    final long[] target = new long[1];
    final long[] counted = new long[1];
    private Sensor stepSensor;
    boolean running = false;
    private TextView number;
    private TextView percent;
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
        sensorManager  = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference().child("StepCounter").child(mAuth.getUid()); //link to Step table of current user of today
        step_chart = findViewById(R.id.tracker_chart);
        number =findViewById(R.id.tracker_txtProgess);
        percent =findViewById(R.id.tracker_txtPercent);
        loadData();
        reset_chart();
        loadWeeklyData();
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
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild(d2s))
                {
                    counted[0] = (long) snapshot.child(d2s).child("counted").getValue();
                    if (snapshot.child(d2s).child("target").getValue() != null)
                    {
                        target[0] = (long) snapshot.child(d2s).child("target").getValue();
                    }
                    else mRef.child(d2s).child("target").setValue(8000);
                    progress(target[0], counted[0]);
                }
                else
                {
                    mRef.child(d2s).child("counted").setValue(0);
                    mRef.child(d2s).child("target").setValue(8000);
                    Log.d(TAG, "New instance set!");
                    progress(8000, 0);
                    prev_step = total_step;
                    counted[0] = 0;
                    progress(target[0], counted[0]);
                    Log.d(TAG, "Reseted");
                    saveData(0);
                }
                Log.d(TAG, "Steps counted on DB: "+counted[0]);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
    }

    private void loadWeeklyData(){
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);
        int year =  Calendar.getInstance().get(Calendar.YEAR);

        Calendar cal= new GregorianCalendar();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        System.out.println("Start of this week:       " + cal.getTime());
        System.out.println("... in milliseconds:      " + cal.getTimeInMillis());

    }

    private void progress(long target, long counted){
        if (target == 0) target = 1; //divide by 0
        float percent =( (float)counted/target)*100;
        this.percent.setText((int) percent + "%");
        number.setText((total_step - prev_step) + " / " + target);
        step_chart.setProgress(percent);
    }



}
