package com.fatguy.behealthy.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Adapters.CovidAdapter;
import com.fatguy.behealthy.Models.Covid.Covid;
import com.fatguy.behealthy.Models.Covid.Data;
import com.fatguy.behealthy.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CovidActivity extends AppCompatActivity {
    private static final String TAG = "CovidActivity";
    TextView total_vn, total_glo, death_vn, death_glo, recovery_glo, recovery_vn, patient_vn, patient_glo;
    TextView search_title;
    ArrayList<Data> listData = new ArrayList<>();
    ListView list;
    Button Search;
    ArrayList<String> listString = new ArrayList<>();
    RecyclerView recyclerView;
    AutoCompleteTextView autoText;
    private Covid data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);
        initData();
        set();
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoText.getText().toString().equals("") || autoText.getText().toString().equals("The place you want to search"))
                    Toast.makeText(CovidActivity.this, "Please enter the location", Toast.LENGTH_SHORT).show();
                else Search();

            }
        });

    }

    private void set() {
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        recyclerView = findViewById(R.id.coivd_view);
        //Vietnam
        total_vn = findViewById(R.id.cov_infected);
        death_vn = findViewById(R.id.cov_deaths);
        recovery_vn = findViewById(R.id.cov_recovered);
        patient_vn = findViewById(R.id.cov_treatment);

        //Global
        total_glo = findViewById(R.id.cov_glo_infected);
        death_glo = findViewById(R.id.cov_glo_deaths);
        recovery_glo = findViewById(R.id.cov_glo_recovered);
        patient_glo = findViewById(R.id.cov_glo_treatment);

        recyclerView.setHasFixedSize(true);
        CovidAdapter covidAdapter = new CovidAdapter();
        recyclerView.setAdapter(covidAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set display data
        //Vietnam
        total_vn.setText(decimalFormat.format(data.covidV3.getCases()));
        death_vn.setText(decimalFormat.format(data.covidV3.getDeaths()));
        recovery_vn.setText(decimalFormat.format(data.covidV3.getRecovered()));
        patient_vn.setText(decimalFormat.format(data.covidV3.getActive()));
        //Global
        total_glo.setText(decimalFormat.format(data.covidV3_glo.getCases()));
        death_glo.setText(decimalFormat.format(data.covidV3_glo.getDeaths()));
        recovery_glo.setText(decimalFormat.format(data.covidV3_glo.getRecovered()));
        patient_glo.setText(decimalFormat.format(data.covidV3_glo.getActive()));

        for (int i = 0; i < data.getCovid19().getData().length; i++) {
            Data add = new Data();
            add.setName(data.getCovid19().getData()[i].getName());
            add.setAge(data.getCovid19().getData()[i].getAge() + "\t");
            add.setAdds(data.getCovid19().getData()[i].getAdds());
            add.setStatus(data.getCovid19().getData()[i].getStatus());
            add.setCountry(data.getCovid19().getData()[i].getCountry());
            covidAdapter.addMessage(add);
        }
        search_title = findViewById(R.id.cov_txtTitleSearch);
        String data_total = data.getCovid19().getTotal();
        search_title.setText(String.format("Recently %s patients in Vietnam:", data_total));

        listString = data.getCovid19().getProvince();
        autoText = findViewById(R.id.Covid_autotext);
        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listString);
        autoText.setAdapter(adapterSearch);
        Search = findViewById(R.id.Covid_btn_Search);
    }

    private void Search() {
        ConstraintLayout mainLayout = findViewById(R.id.covid_ConstraintLayout);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        CovidAdapter covidAdapter = new CovidAdapter();
        recyclerView.setAdapter(covidAdapter);
        for (int i = 0; i < data.getCovid19().getData().length; i++) {
            if (autoText.getText().toString().equals(data.getCovid19().getData()[i].getAdds())) {
                Data add = new Data();
                add.setName(data.getCovid19().getData()[i].getName());
                add.setAge(data.getCovid19().getData()[i].getAge() + "\t");
                add.setAdds(data.getCovid19().getData()[i].getAdds());
                add.setStatus(data.getCovid19().getData()[i].getStatus());
                add.setCountry(data.getCovid19().getData()[i].getCountry());
                covidAdapter.addMessage(add);
            }
        }
    }

    private void initData() {
        Intent i = getIntent();
        data = (Covid) i.getSerializableExtra("data");
        Log.d(TAG, "initData: Total - " + data.getCovid19().getTotal());
    }
}
