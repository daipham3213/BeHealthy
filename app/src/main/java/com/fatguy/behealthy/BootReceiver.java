package com.fatguy.behealthy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private long startTimer;
    private long endTimer;
    private long screenOnTimeSingle;
    private long screenOnTime;
    private final long TIME_ERROR = 1000;

    public void onReceive(Context context, Intent intent) {
        Log.i("BroadcastReceiver", "ScreenTimeService onReceive");

        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            startTimer = System.currentTimeMillis();
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            endTimer = System.currentTimeMillis();
            screenOnTimeSingle = endTimer - startTimer;

            if(screenOnTimeSingle < TIME_ERROR) {
                screenOnTime += screenOnTime;
            }

        }
    }

    public long getScreenOnTimeSingle() {
        return screenOnTimeSingle;
    }

    public long getScreenOnTime() {
        return screenOnTime;
    }
}