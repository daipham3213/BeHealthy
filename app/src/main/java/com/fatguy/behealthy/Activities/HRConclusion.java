package com.fatguy.behealthy.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.fatguy.behealthy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HRConclusion extends Activity {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm");
    String d2s;
    private String BPM;
    private TextView date_time;
    private TextView bpm;
    private TextView state;
    private TextView recommend;
    private IconRoundCornerProgressBar bpm_chart;
    private Button done;
    private CardView state_card;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    String[] adivce = {"\t Your heart rate is too week!\n" +
            "- You should take the consultation from doctor.",
            "\t Your hear rate is normal.\n" +
                    "- You should always aim to take good care of your heart. \n" +
                    "- This includes doing things like exercising regularly, eating a heart-healthy diet, and maintaining a healthy weight.",
            "\t Your Heart rate is too HIGH!\n" +
                    "- Make sure your surroundings are cool and comfortable. High temperatures and humidity can increase blood flow and heart rate.\n" +
                    "- Emotional upset can raise your heart rate. Slow, measured breathing can help bring it back down.\n" +
                    "- If you’re going from sitting to standing, make sure to rise slowly. Standing up too quickly can bring about dizziness and cause your heart rate to increase."
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_conclusion);

        BPM = getIntent().getStringExtra("BPM");
        BPM = BPM.substring(0, BPM.indexOf(" "));
        Date date = Calendar.getInstance().getTime();
        d2s = dateFormat.format(date);

        date_time = findViewById(R.id.hrc_date_time);
        state = findViewById(R.id.hrc_state);
        state_card = findViewById(R.id.hrc_state_card);
        recommend = findViewById(R.id.hrc_recomendation);
        bpm_chart = findViewById(R.id.hrc_chart);
        done = findViewById(R.id.hrc_btnDone);
        bpm = findViewById(R.id.hrc_bpm);

        bpm.setText(BPM);
        date_time.setText(d2s);
        initData(BPM);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HRConclusion.this, MainActivity.class));
            }
        });


    }

    private void initData(String BPM) {
        int beats = Integer.valueOf(BPM);

        if (beats < 60) {
            bpm_chart.setProgressColor(Color.parseColor("#FFE082"));
            bpm_chart.setIconBackgroundColor(Color.parseColor("#FFE082"));
            recommend.setText(adivce[0]);
            state.setText("Low");
            state_card.setCardBackgroundColor(Color.parseColor("#FFE082"));
        } else if (beats < 100) {
            bpm_chart.setProgressColor(Color.parseColor("#80CBC4"));
            bpm_chart.setIconBackgroundColor(Color.parseColor("#80CBC4"));
            recommend.setText(adivce[1]);
            state_card.setCardBackgroundColor(Color.parseColor("#80CBC4"));
            state.setText("Normal");
        } else {
            bpm_chart.setProgressColor(Color.parseColor("#E57373"));
            bpm_chart.setIconBackgroundColor(Color.parseColor("#E57373"));
            recommend.setText(adivce[2]);
            state_card.setCardBackgroundColor(Color.parseColor("#E57373"));
            state.setText("High");
        }

        bpm_chart.setMax(120);
        bpm_chart.setProgress(beats);
        bpm_chart.invalidate();
    }
}
