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

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.fatguy.behealthy.Models.Utils.Format2Read;

public class Diagnose extends AppCompatActivity {
    private static final String TAG = "Diagnose";
    MessageAdapter adapter;
    private TextView btnYes;
    private TextView btnNo;
    private RecyclerView chat_box;
    private String first_symptom;
    private Attribute[] attributes;
    private final List<String> symptoms = new ArrayList<>();
    private final boolean is_yes = false;
    List<String> temp;
    String name;
    private int curr_i = 0;
    private List<String> disease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        initData();
    }

    public void initData() {
        btnYes = findViewById(R.id.chat_btnYes);
        btnNo = findViewById(R.id.chat_btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataFilter(temp, name, true);
                chat_box.scrollToPosition(adapter.getItemCount() - 1);
                diagnose(symptoms.get(symptoms.size() - 1), attributes, ++curr_i);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataFilter(temp, name, false);
                chat_box.scrollToPosition(adapter.getItemCount() - 1);
                diagnose(symptoms.get(symptoms.size() - 1), attributes, ++curr_i);
            }
        });

        chat_box = findViewById(R.id.chat_box);
        Intent diag_data = getIntent();
        adapter = new MessageAdapter();
        chat_box.setAdapter(adapter);
        chat_box.setLayoutManager(new LinearLayoutManager(this));

        attributes = (Attribute[]) diag_data.getSerializableExtra("attrs");
        first_symptom = diag_data.getStringExtra("selection");
        first_symptom = Format2Read(first_symptom, 0);
        Log.d(TAG, "initData: Done");
        disease = findDisease(first_symptom, attributes);
        symptoms.add(first_symptom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        diagnose(first_symptom, attributes, 0);
    }

    public void diagnose(String symptom, @NotNull Attribute[] attrs, int p) {
        if (p >= attrs.length) return;
        if (disease.size() == 1) {
            adapter.addMessage(getSymptom(symptoms), Utils.dateFormat(1), true);
            adapter.addMessage("You may caught: " + disease.get(0), Utils.dateFormat(1), true);
            adapter.addMessage(disease.get(0) + "\n\t" + getDesc(disease.get(0)).trim(), Utils.dateFormat(1), true);
            adapter.addMessage("Precaution: \n" + getPre(disease.get(0)), Utils.dateFormat(1), true);
            chat_box.scrollToPosition(adapter.getItemCount() - 1);
            btnYes.setClickable(false);
            btnNo.setClickable(false);
            return;
        }
        temp = filterDisease(disease, attrs[p].values.get(getPos(attrs[p])).classes);
        if (temp.size() > 0 & !symptom.contains(attrs[p].name)) {
            name = attrs[p].name;
            name = Format2Read(name, 1);
            //Add question
            adapter.addMessage("Do you have " + name + "? ", Utils.dateFormat(1), true);
            chat_box.scrollToPosition(adapter.getItemCount() - 1);
        } else diagnose(symptom, attrs, ++curr_i);
    }

    public String getSymptom(List<String> symptoms) {
        List<String> symp = symptoms;
        Scanner scan;
        ArrayList<String[]> obj = new ArrayList<>();
        String rs = "Symptoms: \n";
        InputStream is = getResources().openRawResource(R.raw.disease_data);
        scan = new Scanner(is);
        String[] line;
        for (int i = 0; i < symp.size(); i++) {
            symp.set(i, Utils.Format2Read(symp.get(i), 0));
        }
        while (scan.hasNext()) {
            line = scan.nextLine().split(",");
            if (disease.get(0).equals(line[0])) {
                obj.add(line);
            }
        }
        for (String[] strs : obj) {
            if (Utils.linearIn(symp, strs)) {
                for (int i = 1; i < strs.length; i++) {
                    if (!rs.contains(strs[i])) {
                        symp.add(strs[i]);
                    }
                }
            }
        }
        for (String st : symp) {
            String temp = "- " + Utils.Format2Read(st.trim(), 1) + "\n";
            if (!rs.contains(temp))
                rs += temp;
        }
        return rs;
    }

    public String getDesc(String disease) {
        Scanner scan;
        InputStream is = getResources().openRawResource(R.raw.symptom_description);
        // start loop for all files HERE
        scan = new Scanner(is);
        String[] line = scan.nextLine().split(",");
        while (scan.hasNext() & !line[0].equals(disease)) {
            line = scan.nextLine().split(",");
        }
        StringBuilder desc = new StringBuilder();
        Utils.Format2Read(line);
        for (int i = 1; i < line.length; i++) {
            desc.append(line[i]).append(", ");
        }
        return desc.toString();
    }

    public String getPre(String disease) {
        Scanner scan;
        InputStream is = getResources().openRawResource(R.raw.symptom_precaution);
        // start loop for all files HERE
        scan = new Scanner(is);
        String[] line = scan.nextLine().split(",");
        while (scan.hasNext() & !line[0].equals(disease)) {
            line = scan.nextLine().split(",");
        }
        StringBuilder precaution = new StringBuilder();
        Utils.Format2Read(line);
        for (int i = 1; i < line.length; i++) {
            precaution.append("\t- ").append(line[i]).append("\n");
        }
        return precaution.toString();
    }

    public void dataFilter(List<String> temp, String name, boolean is_yes) {
        if (is_yes) {
            adapter.addMessage("Yes", Utils.dateFormat(1), false);
            disease = temp;
            symptoms.add(name);
        } else {
            adapter.addMessage("No", Utils.dateFormat(1), false);
        }
    }

    public List<String> findDisease(String symptoms, @NotNull Attribute[] attrs) {
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

    public List<String> filterDisease(@NotNull List<String> list1, List<String> list2) {
        List<String> rs = new ArrayList<>();
        for (String name : list1) {
            if (list2.contains(name)) {
                rs.add(name);
            }
        }
        return rs;
    }

    public int getPos(@NotNull Attribute attribute) {
        int pos = 0;
        if (attribute.values.get(0).valueName.equals("1"))
            pos = 0;
        else if (attribute.values.size() > 1) {
            pos = 1;
        }
        return pos;
    }

}