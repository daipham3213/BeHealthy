package com.fatguy.behealthy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Adapters.CoivdAdpter;
import com.fatguy.behealthy.Models.Covid.Covid19;
import com.fatguy.behealthy.Models.Covid.Data;
import com.fatguy.behealthy.R;

import java.util.ArrayList;

public class CovidActivity extends AppCompatActivity {
    private static final String TAG = "CovidActivity";
    private Covid19 data;
    TextView tolal;
    TextView die;
    TextView recovery;
    ListView list;
    ArrayList<Data> listData= new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> listString= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        data = (Covid19) intent.getSerializableExtra("data");
        setContentView(R.layout.activity_covid);
        initData();
        set();

    }

    private void set() {

        list = findViewById(R.id.covid_list);
        recyclerView = findViewById(R.id.coivd_view);
        tolal = findViewById(R.id.coivid_txt_total);
        die = findViewById(R.id.coivid_txt_die);
        recovery = findViewById(R.id.coivd_txt_recovery);
//        xu li recyclerView
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        for (int i = 0; i < data.getData().length; i++) {
            Data add = new Data();
            add.setName(data.getData()[i].getName());
            add.setAge(data.getData()[i].getAge());
            add.setAdds(data.getData()[i].getAdds());
            add.setStatus(data.getData()[i].getStatus());
            add.setCountry(data.getData()[i].getCountry());

            listData.add(add);
        }
        CoivdAdpter coivdAdpter =new CoivdAdpter(listData,this);
        recyclerView.setAdapter(coivdAdpter);
        // xu li liner

        String datatolal= data.getTotal();
        String datadie= data.getDie();
        String datarecovery=data.getRecovery();

        tolal.setText(tolal.getText()+":"+"\t"+datatolal);
        die .setText(die.getText()+":"+"\t"+datadie);
        recovery .setText(recovery.getText()+":"+"\t"+datarecovery);



    }

    private void initData() {
        Intent i = getIntent();
        data = (Covid19) i.getSerializableExtra("data");
        Log.d(TAG, "initData: Total - " + data.getTotal());
    }
}