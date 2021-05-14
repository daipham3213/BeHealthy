package com.fatguy.behealthy;

import android.app.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suke.widget.SwitchButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


public class ReminderActivity extends Activity {
    private SwitchButton swtWater;
    private SwitchButton swtScreen;
    private Spinner spnWater;
    private Spinner spnScreen;
    private RecyclerView rclWater;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private long onScreen;
    NotificationManagerCompat notifyManage;
    NotificationCompat.Builder notify;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String d2s;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        swtScreen = findViewById(R.id.reminder_swtScreen);
        swtWater = findViewById(R.id.reminder_swtWater);
        spnScreen = findViewById(R.id.reminder_spnScreen);
        spnWater = findViewById(R.id.reminder_spnWater);
        Date date = Calendar.getInstance().getTime();
        d2s = dateFormat.format(date);
        createNotificationChannel();

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Reminder").child(mAuth.getUid());

        notify = new NotificationCompat.Builder(this, "fatguyA")
                .setSmallIcon(R.drawable.ic_smartphone)
                .setContentTitle("Reminder - Alert")
                .setContentText("Don't stare at your screen all day!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notifyManage =  NotificationManagerCompat.from(this);


        LoadList(R.array.screen_time,spnScreen);
        LoadList(R.array.cup_size,spnWater);
        LoadData();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "fatguy Chanel";
            String description = "Reminder - Remind you to have a better health!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("fatguyA", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mRef.child("ScreenTime").child("State").setValue(swtScreen.isChecked());
        mRef.child("ScreenTime").child("Value").setValue(spnScreen.getSelectedItemPosition());

    }

    private void LoadList(int resId, Spinner spn) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                resId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
    }

    private void LoadData(){
        final boolean[] check = new boolean[1];
        mRef.child("ScreenTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.hasChild("State") & snapshot.hasChild("Value")){
                    check[0] = (Boolean) snapshot.child("State").getValue();
                    swtScreen.setChecked(check[0]);
                    long val = (long) snapshot.child("Value").getValue();
                    spnScreen.setSelection(Math.toIntExact(val));
                    onScreen = (long) snapshot.child("OnScreen").child(d2s).getValue();
                    long hoursOS = TimeUnit.MILLISECONDS.toMinutes(onScreen);
                    if (check[0] & (spnScreen.getSelectedItemPosition()+2 <= hoursOS))
                    {
                        notifyManage.notify(100,notify.build());
                    }
                }
                else {
                    mRef.child("ScreenTime").child("State").setValue(false);
                    mRef.child("ScreenTime").child("Value").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

}
