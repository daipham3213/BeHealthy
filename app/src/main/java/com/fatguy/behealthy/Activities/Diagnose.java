package com.fatguy.behealthy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Adapters.MessageAdapter;
import com.fatguy.behealthy.Models.C45.Attribute;
import com.fatguy.behealthy.Models.Utils;
import com.fatguy.behealthy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Diagnose extends AppCompatActivity {
    private static final String TAG = "Diagnose";
    List<String> disease;
    List<String> symptoms = new ArrayList<>();
    MessageAdapter adapter;
    private TextView btnYes;
    private TextView btnNo;
    private RecyclerView chat_box;
    private String first_symptom;
    private Attribute[] attributes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        diagnose(first_symptom, attributes);
    }

    public void initData() {
        btnYes = findViewById(R.id.chat_btnYes);

        btnNo = findViewById(R.id.chat_btnNo);
        chat_box = findViewById(R.id.chat_box);
        Intent diag_data = getIntent();
        adapter = new MessageAdapter();
        chat_box.setAdapter(adapter);
        chat_box.setLayoutManager(new LinearLayoutManager(this));

        attributes = (Attribute[]) diag_data.getSerializableExtra("attrs");
        first_symptom = diag_data.getStringExtra("selection");
        Log.d(TAG, "initData: Done");
    }

    public void diagnose(String attr, Attribute[] attrs) {
        symptoms.add(attr);
        disease = findDisease(attr, attrs);
        for (Attribute attribute : attrs) {
            List<String> temp = filterDisease(disease, attribute.values.get(getPos(attribute)).classes);
            if (!temp.isEmpty()) {
                String name = attribute.name;
                //Add question
                adapter.addMessage("Do you have " + name + "? ", Utils.dateFormat(1), true);
                //Add answer
                final boolean[] is_click = {false};
                while (!is_click[0]) {
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.addMessage("Yes", Utils.dateFormat(1), false);
                            disease = temp;
                            symptoms.add(name);
                            is_click[0] = true;
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.addMessage("No", Utils.dateFormat(1), false);
                            is_click[0] = true;
                        }
                    });
                }
            }
            if (disease.size() == 1) break;
        }
        adapter.addMessage("You may caught: " + disease.get(0), Utils.dateFormat(1), true);
    }

    public List<String> findDisease(String symptoms, Attribute[] attrs) {
        int pos = 0;
        List<String> disease = new ArrayList<>();
        for (Attribute item : attrs) {
            if (Objects.equals(item.name, symptoms)) {
                pos = getPos(item);
                disease = item.values.get(pos).classes;
                break;
            }
        }
        return disease;
    }

    public List<String> filterDisease(List<String> list1, List<String> list2) {
        List<String> rs = new ArrayList<>();
        for (String name : list1) {
            if (list2.contains(name)) {
                rs.add(name);
            }
        }
        return rs;
    }

    public int getPos(Attribute attribute) {
        int pos = 0;
        if (attribute.values.get(0).valueName.equals("1"))
            pos = 0;
        else pos = 1;
        return pos;
    }
}