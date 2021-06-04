package com.fatguy.behealthy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fatguy.behealthy.Models.C45.Attribute;
import com.fatguy.behealthy.Models.C45.Diagnoser;
import com.fatguy.behealthy.R;

public class Diagnose extends AppCompatActivity {
    private static final String TAG = "Diagnose";
    static String symptoms;
    private TextView bot;
    private TextView user;
    private Button Yes;
    static String BotChat = "Hello\n\n";
    static String UserChat = "\n";
    private Button No;
    static String boolen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaggonsis);
        initData();
        user.setText(UserChat);
        bot.setText(BotChat);

        if (symptoms != null && !symptoms.equals("")) {
            Yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolen = "1";
                    UserChat = UserChat + "\n Yes\n";
                    user.setText(UserChat);
                }
            });
            No.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolen = "0";
                    UserChat = UserChat + "\n No\n";
                    user.setText(UserChat);
                }
            });
        }

    }

    public void initData() {
        bot = findViewById(R.id.Diaggonsi_txt_Bot);
        user = findViewById(R.id.Diaggonsi_txt_user);
        Yes = findViewById(R.id.Diaggonsi_btn_yes);
        No = findViewById(R.id.Diaggonsi_btn_No);

        Intent diag = getIntent();
        Attribute[] attributes = (Attribute[]) diag.getSerializableExtra("attrs");
        Log.d(TAG, "initData: Done");
        Diagnoser diagnoser = new Diagnoser();
        diagnoser.diagnose("itching", attributes);
    }
}