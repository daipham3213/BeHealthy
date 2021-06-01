package com.fatguy.behealthy.Activities;

import android.app.Activity;
import android.content.Intent;
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
    private int[] colors;
    private Button done;
    private CardView state_card;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_conclusion);

        BPM = getIntent().getStringExtra("BPM");
        BPM = BPM.substring(0, BPM.indexOf(" "));
        colors = new int[]{R.color.md_amber_200, R.color.md_teal_200, R.color.md_red_300};
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
            bpm_chart.setProgressColor(colors[0]);
            state.setText("Low");
            state_card.setCardBackgroundColor(colors[0]);
        } else if (beats < 100) {
            bpm_chart.setProgressColor(colors[1]);
            state_card.setCardBackgroundColor(colors[1]);
            state.setText("Normal");
        } else {
            bpm_chart.setProgressColor(colors[2]);
            state_card.setCardBackgroundColor(colors[2]);
            state.setText("High");
        }

        bpm_chart.setMax(120);
        bpm_chart.setProgress(beats);
        bpm_chart.setSecondaryProgress(beats * 1.1f);
        bpm_chart.invalidate();
    }
}
