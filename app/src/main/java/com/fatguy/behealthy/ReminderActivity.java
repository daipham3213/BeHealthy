package com.fatguy.behealthy;

import android.app.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suke.widget.SwitchButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private User user;
    float waterAmount;
    long consumed;
    long cupSize;
    private ArrayList<Integer> hour;
    private ArrayList <Integer> min;
    private ArrayList <String> cups;
    int hourStart = 7;
    int minStart = 30;
    private IconRoundCornerProgressBar waterBar;


    NotificationManagerCompat notifyManage;
    NotificationCompat.Builder notify;
    NotificationCompat.Builder water_notify;
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
        waterBar = findViewById(R.id.reminder_water_chart);

        createNotificationChannel();

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Reminder").child(mAuth.getUid());

        notify = new NotificationCompat.Builder(this, "fatguyA")
                .setSmallIcon(R.drawable.ic_smartphone)
                .setContentTitle("Reminder - Alert")
                .setContentText("Don't stare at your screen all day!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        PendingIntent waterIntentLater = PendingIntent.
                getActivity(this,0,
                        new Intent(this, ReminderActivity.class),
                        PendingIntent.FLAG_ONE_SHOT);
        PendingIntent waterIntentOk = PendingIntent.
                getActivity(this,0,
                        new Intent(this, ReminderActivity.class),
                        PendingIntent.FLAG_ONE_SHOT);

        water_notify = new NotificationCompat.Builder(this, "fatguyA")
                .setSmallIcon(R.drawable.ic_glass_of_water)
                .setContentTitle("Reminder - Alert")
                .setContentText("Don't forget to drink lots of water!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_glass_of_water, "Later",waterIntentLater)
                .addAction(R.drawable.ic_glass_of_water, "OK",waterIntentOk);

        notifyManage =  NotificationManagerCompat.from(this);
        user = new User();

        LoadList(R.array.screen_time,spnScreen);
        LoadList(R.array.cup_size,spnWater);
        LoadData();

        spnWater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisplaySchedule(waterAmount-consumed,(position + 1)*100);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swtWater.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    hourStart = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    minStart =Calendar.getInstance().get(Calendar.MINUTE);
                }
            }
        });
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
        mRef.child("DrinkWater").child("Enable").setValue(swtWater.isChecked());
        mRef.child("DrinkWater").child("CupSize").setValue((spnWater.getSelectedItemPosition()+1)*100);
        mRef.child("DrinkWater").child("Hour").setValue(hourStart);
        mRef.child("DrinkWater").child("Min").setValue(minStart);
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

         DatabaseReference User = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
         User.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                 user.setSex(snapshot.child("sex").getValue(String.class));
                 user.setAge(snapshot.child("age").getValue(Integer.class));
                 user.setWeight(snapshot.child("weight").getValue(Float.class));
             }

             @Override
             public void onCancelled(@NonNull @NotNull DatabaseError error) {

             }
         });

        mRef.child("DrinkWater").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.hasChild("WaterAmount") & snapshot.child("WaterAmount").getValue(Long.class)!=null) {
                    waterAmount = snapshot.child("WaterAmount").getValue(Float.class);
                } else mRef.child("DrinkWater").child("WaterAmount").setValue(getWaterAmount(user.getWeight(),user.getAge()));

                if (snapshot.child(d2s).hasChild("Consumed")){
                    consumed = snapshot.child(d2s).child("Consumed").getValue(Long.TYPE);
                } else mRef.child("DrinkWater").child(d2s).child("Consumed").setValue(0);

                if (snapshot.hasChild("CupSize")){
                    cupSize = snapshot.child("CupSize").getValue(Long.class);
                } else mRef.child("DrinkWater").child("CupSize").setValue(100);

                if (snapshot.hasChild("Enable")){
                    swtWater.setChecked(snapshot.child("Enable").getValue(Boolean.class));
                } else mRef.child("DrinkWater").child("Enable").setValue(false);

                if (snapshot.hasChild("Hour")){
                    hourStart = snapshot.child("Hour").getValue(Integer.class);
                } else mRef.child("DrinkWater").child("Hour").setValue(7);

                if (snapshot.hasChild("Min")){
                    minStart =snapshot.child("Min").getValue(Integer.class);
                } else mRef.child("DrinkWater").child("Hour").setValue(30);

                progress(waterAmount,consumed);
                spnWater.setSelection((int) (cupSize/100-1));

                DisplaySchedule(waterAmount-consumed,cupSize);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void DisplaySchedule (float amount, long cupSize){
        scheduleUp(amount,cupSize);
        rclWater = findViewById(R.id.reminder_rclWater);
        TimeStampAdapter schedule = new TimeStampAdapter(ReminderActivity.this,hour,min,cups);

        rclWater.setAdapter(schedule);
        rclWater.setLayoutManager(new LinearLayoutManager(ReminderActivity.this));
    }

    private float getWaterAmount(float weight, int age){
        float amount = 0;
        if (age<30) amount =  (weight*40);
        if (age>29 & age<55) amount =  (weight*35);
        if (age>55) amount =  (weight*30);
        amount = (float) (amount / (28.3*33.8));
        return amount*1000;
    }

    private void progress(float target, float counted){
        if (target == 0) target = 1; //divide by 0
        float percent =( (float)counted/target)*100;
//        this.percent.setText(String.valueOf((int)percent)+"%");
//        number.setText(total_step+" / "+target);
        waterBar.setProgress(percent);
    }

    private void scheduleUp (float amount, long cupSize){
        int h = hourStart;
        int m = minStart;

        cups = new ArrayList<>();
        hour = new ArrayList<>();
        min = new ArrayList<>();
        float temp = amount;
        while (temp > 0) {
            while (temp < cupSize && cupSize > 0) cupSize -= 100;
            if (cupSize <=0) {
                cups.add(Math.round(temp) +"");
                temp = 0;
            } else
            {
                cups.add(cupSize+"");
                temp -= cupSize;
            }
            hour.add(h);
            min.add(m);
            h++;
            if (h > 23) {
                h = h - 24;
            }
        }
    }
}
