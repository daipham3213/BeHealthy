package com.fatguy.behealthy.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.fatguy.behealthy.Activities.HospitalActivity;
import com.fatguy.behealthy.Models.gmap.JSONgmap;
import com.fatguy.behealthy.Models.gmap.geometry;
import com.fatguy.behealthy.Models.gmap.northeast;
import com.fatguy.behealthy.Models.gmap.opening_hours;
import com.fatguy.behealthy.Models.gmap.plus_code;
import com.fatguy.behealthy.Models.gmap.results;
import com.fatguy.behealthy.Models.gmap.viewport;

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

public class GetMapData extends AsyncTask<String, Void, JSONgmap> {
    private final String TAG = "GETDataSync";
    JSONObject jsonRoot = null;
    JSONgmap map;
    JSONArray res;
    ArrayList<results> results;
    String url;
    JSONgmap output = new JSONgmap();
    private final Context context;
    private ProgressDialog progDailog;

    public GetMapData(Context context) {
        this.context = context;
    }

    @Override
    protected JSONgmap doInBackground(String... url) {
        try {
            this.url = url[0];
            Log.d(TAG, "doInBackground: Started");
            getData();
            map = new JSONgmap();
            if (jsonRoot != null) {
                Log.d(TAG, "doInBackground: jsonData is " + jsonRoot.getString("status"));
                res = jsonRoot.optJSONArray("results");
                //map.setNewResults(res.length());
                results = new ArrayList<>();
                map.setStatus(jsonRoot.getString("status"));
                for (int i = 0; i < res.length(); i++) {
                    JSONObject resNode = res.getJSONObject(i);
                    if (resNode != null) {
                        results obj = new results();
                        //Get values
                        if (!resNode.isNull("business_status"))
                            obj.setBusiness_status(resNode.getString("business_status"));
                        obj.setName(resNode.getString("name"));
                        obj.setPlace_id(resNode.getString("place_id"));
                        obj.setVicinity(resNode.getString("vicinity"));
                        obj.setIcon(resNode.getString("icon"));
                        obj.setReference(resNode.getString("reference"));
                        //Geometry
                        JSONObject geoNode = resNode.getJSONObject("geometry");
                        JSONObject locNode = geoNode.getJSONObject("location");
                        JSONObject viewNode = geoNode.getJSONObject("viewport");
                        JSONObject northNode = viewNode.getJSONObject("northeast");
                        JSONObject southNode = viewNode.getJSONObject("southwest");

                        northeast location = new northeast(locNode.getDouble("lat"), locNode.getDouble("lng"));
                        northeast northeast = new northeast(northNode.getDouble("lat"), northNode.getDouble("lng"));
                        northeast southwest = new northeast(southNode.getDouble("lat"), southNode.getDouble("lng"));

                        viewport viewport = new viewport(northeast, southwest);
                        geometry geometry = new geometry(location, viewport);
                        obj.setGeometry(geometry);//Done Geometry

                        //Plus_code
                        if (!resNode.isNull("plus_code")) {
                            JSONObject plus_codeNode = resNode.getJSONObject("plus_code");
                            plus_code plus_code = new plus_code(plus_codeNode.getString("compound_code"), plus_codeNode.getString("global_code"));
                            obj.setPlus_code(plus_code);
                        }
                        //Open_hours
                        if (!resNode.isNull("opening_hours")) {
                            JSONObject openNode = resNode.getJSONObject("opening_hours");
                            opening_hours isOpen = new opening_hours(openNode.getBoolean("open_now"));
                            obj.setOpening_hours(isOpen);
                        }
                        //Save values

                        if (!resNode.isNull("rating"))
                            obj.setRating(resNode.getDouble("rating"));
                        results.add(obj);
                    }
                }
                // map.setResults(results);
                Log.d(TAG, "doInBackground: MapDATA is " + map.getStatus());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
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

    @Override
    protected void onPostExecute(JSONgmap map) {
        super.onPostExecute(map);
        if (progDailog.isShowing()) {
            progDailog.dismiss();
        }
        Log.d(TAG, "onPostExecute: " + map.getStatus());
        Intent start_hospital = new Intent(context, HospitalActivity.class);
        map.setResults(results.toArray(new results[0]));
        start_hospital.putExtra("gmap", map);
        context.startActivity(start_hospital);
    }


    private void getData() throws IOException, JSONException {
        jsonRoot = readJsonFromUrl(url);
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

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            jsonRoot = json;
            return json;
        } finally {
            is.close();
        }
    }

}
