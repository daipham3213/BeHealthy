package com.fatguy.behealthy.Models;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.fatguy.behealthy.Models.gmap.JSONgmap;
import com.fatguy.behealthy.Models.gmap.results;
import com.fatguy.behealthy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class getData {
    final String TAG = "getData";
    private final Context context;
    private final String lat;
    private final String lng;
    private final long Radius;
    private final String types;
    String jsonText;
    JSONObject jsonRoot = null;
    JSONgmap map = new JSONgmap();
    JSONArray res;
    results[] results;
    private final String url;
    JSONgmap output = new JSONgmap();

    public getData(Context context, double lng, double lat, long radius, String types) {
        this.context = context;
        this.lat = String.valueOf(lat);
        this.lng = String.valueOf(lng);
        Radius = radius;
        this.types = types;
        //URL GMap Place api
        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=" + radius + "&types=" + types + "&key=" + context.getString(R.string.api_key);

    }

    public JSONgmap getMap() throws ExecutionException, InterruptedException {
        if (checkInternetConnection()) {
            GetDataSync getDataSync = (GetDataSync) new GetDataSync(url).execute();
            output = getDataSync.get();
            output.setResults(getDataSync.results);
            Log.d(TAG, "getDataContructor: getMap - " + output.getStatus());
        }
        return output;
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


}
