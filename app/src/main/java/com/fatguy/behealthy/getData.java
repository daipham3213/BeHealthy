package com.fatguy.behealthy;


import  com.fatguy.behealthy.gmap.*;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class getData {
    private Context context;
    private String lat ,lng;
    private long Radius;
    private String types;
    private String url;
    String jsonText;
    JSONObject jsonRoot = null;
    JSONgmap map = new JSONgmap();
    JSONArray res;
    results[] results;

    public getData(Context context,  double lng, double lat,long radius, String types) {
        this.context = context;
        this.lat = String.valueOf(lat);
        this.lng = String.valueOf(lng);
        Radius = radius;
        this.types = types;

        //URL sample
        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius="+radius+"&types="+types+"&key="+context.getString(R.string.api_key);

    }

    public JSONgmap taskStart(){
        new GetDataSync().execute();
        return map;
    }
    private JSONgmap getMap() throws JSONException {
        if (jsonRoot!=null){
            res = jsonRoot.optJSONArray("results");
            results = new results[res.length()];
            map.setStatus(jsonRoot.getString("status"));
            for (int i =0; i<results.length; i++)
            {
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

                northeast location = new northeast(locNode.getDouble("lat"),locNode.getDouble("lng"));
                northeast northeast = new northeast(northNode.getDouble("lat"),northNode.getDouble("lng"));
                northeast southwest = new northeast(southNode.getDouble("lat"),southNode.getDouble("lng"));

                viewport viewport = new viewport(northeast,southwest);
                geometry geometry = new geometry(location,viewport); //Done Geometry

                //Plus_code
                JSONObject plus_codeNode = resNode.getJSONObject("plus_code");
                plus_code plus_code = new plus_code(plus_codeNode.getString("compound_code"),plus_codeNode.getString("global_code"));

                //Open_hours
                JSONObject openNode = resNode.getJSONObject("opening_hours");
                opening_hours isOpen = new opening_hours(openNode.getBoolean("open_now"));

                //Save values
                results[i].setGeometry(geometry);
                results[i].setPlus_code(plus_code);
                results[i].setOpening_hours(isOpen);
                results[i].setRating(resNode.getDouble("rating"));
            }
            map.setResults(results);
        }
        return map;
    }

    private boolean checkInternetConnection() {
        // Get Connectivity Manager
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Details about the currently active default data network
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(context, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(context, "Network is not connected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(context, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(context, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }

    public class GetDataSync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getData();
                map = getMap();
                map.setResults(results);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
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
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            Log.d("TAG", "readJsonFromUrl: " + jsonText);
            jsonRoot = json;
            return json;
        } finally {
            is.close();
        }
    }

}
