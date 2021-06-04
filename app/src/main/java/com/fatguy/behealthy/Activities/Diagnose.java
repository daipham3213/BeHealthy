package com.fatguy.behealthy.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fatguy.behealthy.Models.C45.C45;
import com.fatguy.behealthy.Models.C45.Diagnoser;
import com.fatguy.behealthy.R;

public class Diagnose extends AppCompatActivity {
    TextView bot;
    TextView user;
    Button Yes;
    Button No;

    void set() {
        bot = findViewById(R.id.Diaggonsi_txt_Bot);
        user = findViewById(R.id.Diaggonsi_txt_user);
        Yes = findViewById(R.id.Diaggonsi_btn_yes);
        No = findViewById(R.id.Diaggonsi_btn_No);

    }
    static String BotChat= "Hello\n\n";
    static String UserChat="\n";
    static String symtoms ;
    static String boolen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaggonsis);
        set();
        user.setText(UserChat);
        bot.setText(BotChat);
        C45 c45 = new C45(this);
        Diagnoser  diagnoser = new Diagnoser();

        diagnoser.diagnose("itching",c45.doInBackground());

        if(symtoms!= null && symtoms != "") {
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
}