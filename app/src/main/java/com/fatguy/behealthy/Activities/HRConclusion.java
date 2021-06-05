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
import com.fatguy.behealthy.Models.Utils;
import com.fatguy.behealthy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class HRConclusion extends Activity {

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
    String[] adivce = {"Your heart rate is too week!\n" +
            "You should take the consultation from doctor.",
            "Your hear rate is normal.\n" +
                    "You should always aim to take good care of your heart. This includes doing things like exercising regularly, eating a heart-healthy diet, and maintaining a healthy weight.",
            "Your Heart rate is too HIGH!\n" +
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
        colors = new int[]{R.color.md_amber_200, R.color.md_teal_200, R.color.md_red_300};


        date_time = findViewById(R.id.hrc_date_time);
        state = findViewById(R.id.hrc_state);
        state_card = findViewById(R.id.hrc_state_card);
        recommend = findViewById(R.id.hrc_recomendation);
        bpm_chart = findViewById(R.id.hrc_chart);
        done = findViewById(R.id.hrc_btnDone);
        bpm = findViewById(R.id.hrc_bpm);

        bpm.setText(BPM);
        date_time.setText(Utils.dateFormat(1));
        initData(BPM);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HRConclusion.this, MainActivity.class));
            }
        });


    }

    private void initData(String BPM) {
        int beats = Integer.parseInt(BPM);


        if (beats < 60) {
            bpm_chart.setProgressColor(colors[0]);
            recommend.setText(adivce[0]);
            state.setText("Low");
            state_card.setCardBackgroundColor(colors[0]);
        } else if (beats < 100) {
            bpm_chart.setProgressColor(colors[1]);
            recommend.setText(adivce[1]);
            state_card.setCardBackgroundColor(colors[1]);
            state.setText("Normal");
        } else {
            bpm_chart.setProgressColor(colors[2]);
            recommend.setText(adivce[2]);
            state_card.setCardBackgroundColor(colors[2]);
            state.setText("High");
        }

        bpm_chart.setMax(120);
        bpm_chart.setProgress(beats);
        bpm_chart.invalidate();
    }
}
