package com.fatguy.behealthy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Adapters.CovidAdapter;
import com.fatguy.behealthy.Models.Covid.Covid19;
import com.fatguy.behealthy.Models.Covid.Data;
import com.fatguy.behealthy.Models.Covid.GetCovid19;
import com.fatguy.behealthy.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CovidActivity extends AppCompatActivity {
    private static final String TAG = "CovidActivity";
    private Covid19 data;
    TextView tolal;
    TextView die;
    TextView recovery;
    ListView list;
    Button Search;
    ArrayList<Data> listData= new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> listString= new ArrayList<>();
    AutoCompleteTextView autoText ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);
        initData();
        set();
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoText.getText().equals("")||autoText.getText().equals("The place you want to search"))
                    Toast.makeText(CovidActivity.this, "Please enter the location", Toast.LENGTH_SHORT).show();
                else Search();
   
            }
        });

    }

    private void set(){

        recyclerView = findViewById(R.id.coivd_view);
        tolal = findViewById(R.id.coivid_txt_total);
        die = findViewById(R.id.coivid_txt_die);
        recovery = findViewById(R.id.coivd_txt_recovery);
//        xu li recyclerView
        recyclerView.setHasFixedSize(true);
        CovidAdapter covidAdapter = new CovidAdapter();
        recyclerView.setAdapter(covidAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        for (int i = 0; i < data.getData().length; i++) {
            Data add = new Data();
            add.setName(data.getData()[i].getName());
            add.setAge(data.getData()[i].getAge()+"\t");
            add.setAdds(data.getData()[i].getAdds());
            add.setStatus(data.getData()[i].getStatus());
            add.setCountry(data.getData()[i].getCountry());
            covidAdapter.addMessage(add);
        }

        String datatolal= data.getTotal();
        String datadie= data.getDie();
        String datarecovery=data.getRecovery();
//
        tolal.setText(tolal.getText()+":"+"\t"+datatolal);
        die .setText(die.getText()+":"+"\t"+datadie);
        recovery .setText(recovery.getText()+":"+"\t"+datarecovery);

        /// AutoText//Lỗi
//        listString = data.getProvince();
//        ArrayAdapter adapterSearch = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listString);
//        autoText.setAdapter(adapterSearch);
        Search= findViewById(R.id.Covid_btn_Search);
        ///
    }

    private void Search()
    {
        CovidAdapter covidAdapter = new CovidAdapter();
        recyclerView.setAdapter(covidAdapter);
        for (int i = 0; i < data.getData().length; i++) {
            if(autoText.getText().equals(data.getData()[i].getAdds())) {
                Data add = new Data();
                add.setName(data.getData()[i].getName());
                add.setAge(data.getData()[i].getAge() + "\t");
                add.setAdds(data.getData()[i].getAdds());
                add.setStatus(data.getData()[i].getStatus());
                add.setCountry(data.getData()[i].getCountry());
                covidAdapter.addMessage(add);
            }
        }


    }
    private void initData() {
        Intent i = getIntent();
        data = (Covid19) i.getSerializableExtra("data");
        Log.d(TAG, "initData: Total - " + data.getTotal());
    }
//
//    private String[] place (){
//        listString = data.getProvince();
//
//    }


}
