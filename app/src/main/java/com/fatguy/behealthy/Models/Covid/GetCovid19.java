package com.fatguy.behealthy.Models.Covid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.fatguy.behealthy.Activities.CovidActivity;
import com.fatguy.behealthy.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okhttp3.Response;

public class GetCovid19 extends AsyncTask<String, Void, Covid> {
    private static final String TAG = "GetCovid19";
    private final Context context;
    Covid statistic = new Covid();
    ProgressDialog progDailog;
    private String url;
    private ArrayList<String> province;
    Response response;

    public GetCovid19(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    private static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    @NotNull
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Covid covid19) {
        super.onPostExecute(covid19);
        if (progDailog.isShowing()) {
            progDailog.dismiss();
        }
        Intent start_covid = new Intent(context, CovidActivity.class);
        start_covid.putExtra("data", covid19);
        context.startActivity(start_covid);
    }

    @Override
    protected Covid doInBackground(String... url) {
        try {
            this.statistic = new Covid();
            JSONObject jsonCovid19 = getData(url[0]);
            JSONObject jsonCovidV3 = getData(url[1]);
            JSONObject jsonCovidGlobal = getData(url[2]);

            this.statistic.setCovid19(getCovid19(jsonCovid19));
            this.statistic.setCovidV3(getCovidV3(jsonCovidV3));
            this.statistic.setCovidV3_glo(getCovidV3(jsonCovidGlobal));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return statistic;
    }

    private Covid19 getCovid19(JSONObject jsonRoot) throws JSONException, IOException {
        Covid19 statistic = new Covid19();
        if (jsonRoot != null) {
            statistic.setTotal(jsonRoot.getString("Total"));
            statistic.setDie(jsonRoot.getString("Die"));
            statistic.setRecovery(jsonRoot.getString("Recovery"));

            JSONArray data = jsonRoot.optJSONArray("Data");
            assert data != null;
            Data[] arrg = new Data[data.length()];
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj_json = data.getJSONObject(i);
                String name = obj_json.getString("Name");
                String age = obj_json.getString("Age");
                String adds = obj_json.getString("Adds");
                String status = obj_json.getString("Status");
                String country = obj_json.getString("Country");
                Data obj_data = new Data(name, age, adds, status, country);
                arrg[i] = obj_data;
            }
            statistic.setData(arrg);
        }
        statistic.setProvince(readProvince(context));
        return statistic;
    }

    private CovidV3 getCovidV3(JSONObject jsonRoot) throws JSONException {
        CovidV3 statistic = new CovidV3();
        if (jsonRoot != null) {
            statistic.setCases(jsonRoot.getDouble("cases"));
            statistic.setDeaths(jsonRoot.getDouble("deaths"));
            statistic.setRecovered(jsonRoot.getDouble("recovered"));
            statistic.setActive(jsonRoot.getDouble("active"));
        }
        return statistic;
    }

    private JSONObject getData(String url) throws IOException, JSONException {
        return readJsonFromUrl(url);
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String jsonText = readAll(rd);
                return new JSONObject(jsonText);
            } finally {
                is.close();
            }
        }
    }

    public ArrayList<String> readProvince(Context context) throws IOException, JSONException {
        String jsonText = readText(context, R.raw.province);
        JSONObject jsonRoot = new JSONObject(jsonText);
        ArrayList<String> province = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (jsonRoot.has(String.valueOf(i))) {
                JSONObject obj = jsonRoot.getJSONObject(String.valueOf(i));
                province.add(obj.getString("name"));
            }
        }
        return province;
    }
}
