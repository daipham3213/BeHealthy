package com.fatguy.behealthy.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.fatguy.behealthy.Fragments.MainFragment;
import com.fatguy.behealthy.Fragments.TopBarMainFragment;
import com.fatguy.behealthy.Models.Utils;
import com.fatguy.behealthy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager mng = getSupportFragmentManager();
    private FirebaseAuth mAuth;
    private long startTimer;
    private long endTimer;
    private final long screenOnTimeSingle = 0;
    private long screenTime = 0;
    private long screenTimeDB = 0;
    private final long TIME_ERROR = 1000;
    private Intent mainIntent;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private String d2s;

    private final BroadcastReceiver screen = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", "ScreenTimeService onReceive");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                startTimer = System.currentTimeMillis();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                endTimer = System.currentTimeMillis();
                screenTime = endTimer - startTimer;

                if (screenOnTimeSingle > TIME_ERROR) {
                    screenTime = screenTime + screenOnTimeSingle;
                    Log.d(TAG, "Screen time: " + screenTime);
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTimer = System.currentTimeMillis();
        d2s = Utils.dateFormat(0);
        Initial_Main();

    }

    public void Initial_Main() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            LoginActivity login = new LoginActivity(mAuth);
            this.finish();
            mainIntent = new Intent(MainActivity.this, login.getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
        } else {
            mRef = mRef.child("Reminder").child(mAuth.getUid()).child("ScreenTime").child("OnScreen");

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(d2s)) {
                        SharedPreferences pref = getSharedPreferences(TAG, MODE_PRIVATE);
                        screenTimeDB = snapshot.child(d2s).getValue(Long.TYPE);
                        pref.edit().putLong("OnScreen", screenTimeDB).apply();
                    } else mRef.child(d2s).setValue(0);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
            SharedPreferences pref = getSharedPreferences(TAG, MODE_PRIVATE);
            screenTimeDB = pref.getLong("OnScreen", 0);
            Log.d(TAG, "Initial_Main: screenTimeDB - " + screenTimeDB);
            IntentFilter lockFilter = new IntentFilter();
            lockFilter.addAction(Intent.ACTION_SCREEN_ON);
            lockFilter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(screen, lockFilter);
            render_main();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this, new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACTIVITY_RECOGNITION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND
                        },
                1);
    }

    protected void render_main() {
        MainFragment mf = new MainFragment();
        View view = mf.getRoot();
        TopBarMainFragment tf = new TopBarMainFragment();
        mng.beginTransaction().replace(R.id.layoutMain, mf, mf.getTag()).commit();
        mng.beginTransaction().replace(R.id.layout_top_nav, tf, tf.getTag()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenTimeDB = screenTimeDB + screenTime;
        mRef.child(d2s).setValue(screenTimeDB);
        SharedPreferences pref = getSharedPreferences(TAG, MODE_PRIVATE);
        pref.edit().putLong("OnScreen", screenTimeDB).apply();
        Log.d(TAG, "onStop: screenTimeDB - " + screenTimeDB);
    }
}