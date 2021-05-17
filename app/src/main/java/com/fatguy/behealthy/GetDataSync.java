package com.fatguy.behealthy;

import android.os.AsyncTask;
import android.util.Log;

import com.fatguy.behealthy.gmap.JSONgmap;
import com.fatguy.behealthy.gmap.geometry;
import com.fatguy.behealthy.gmap.northeast;
import com.fatguy.behealthy.gmap.opening_hours;
import com.fatguy.behealthy.gmap.plus_code;
import com.fatguy.behealthy.gmap.results;
import com.fatguy.behealthy.gmap.viewport;

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

public class GetDataSync extends AsyncTask<JSONgmap, Void, JSONgmap> {
    private final String TAG = "GETDataSync";
    JSONObject jsonRoot = null;
    JSONgmap map = new JSONgmap();
    JSONArray res;
    results[] results;
    String url;
    JSONgmap output = new JSONgmap();

    public GetDataSync(String url) {
        this.url = url;
    }

    public JSONgmap getOutput() {

        return output;
    }


    @Override
    protected JSONgmap doInBackground(JSONgmap... jsonGmaps) {
        try {
            Log.d(TAG, "doInBackground: Started");
            getData();
            if (jsonRoot != null) {
                Log.d(TAG, "doInBackground: jsonData is " + jsonRoot.getString("status"));
                res = jsonRoot.optJSONArray("results");
                results = new results[res.length()];
                map.setStatus(jsonRoot.getString("status"));
                for (int i = 0; i < results.length; i++) {
                    JSONObject resNode = res.getJSONObject(i);
                    results[i] = new results();
                    //Get values
                    String bstt = resNode.getString("business_status");
                    if (bstt != null)
                        results[i].setBusiness_status(bstt);
                    results[i].setName(resNode.getString("name"));
                    results[i].setPlace_id(resNode.getString("place_id"));
                    results[i].setVicinity(resNode.getString("vicinity"));
                    results[i].setIcon(resNode.getString("icon"));
                    results[i].setReference(resNode.getString("reference"));
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
                    results[i].setGeometry(geometry);//Done Geometry

                    //Plus_code
                    if (!resNode.isNull("plus_code")) {
                        JSONObject plus_codeNode = resNode.getJSONObject("plus_code");
                        plus_code plus_code = new plus_code(plus_codeNode.getString("compound_code"), plus_codeNode.getString("global_code"));
                        results[i].setPlus_code(plus_code);
                    }
                    //Open_hours
                    if (!resNode.isNull("opening_hours")) {
                        JSONObject openNode = resNode.getJSONObject("opening_hours");
                        opening_hours isOpen = new opening_hours(openNode.getBoolean("open_now"));
                        results[i].setOpening_hours(isOpen);
                    }
                    //Save values

                    if (!resNode.isNull("rating"))
                        results[i].setRating(resNode.getDouble("rating"));
                }
                map.setResults(results);
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
    protected void onPostExecute(JSONgmap map) {
        super.onPostExecute(map);
        output = map;
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
