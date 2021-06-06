package com.fatguy.behealthy.Models;


import android.content.Context;

import com.fatguy.behealthy.Models.gmap.JSONgmap;
import com.fatguy.behealthy.Models.gmap.results;
import com.fatguy.behealthy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.fatguy.behealthy.Models.Utils.checkInternetConnection;


public class getMap {
    final String TAG = "getData";
    private final Context context;
    private final String lat;
    private final String lng;
    private final long Radius;
    private final String types;
    private final String url;
    String jsonText;
    JSONObject jsonRoot = null;
    JSONgmap map = new JSONgmap();
    JSONArray res;
    results[] results;
    JSONgmap output = new JSONgmap();

    public getMap(Context context, double lat, double lng, long radius, String types) {
        this.context = context;
        this.lat = String.valueOf(lat);
        this.lng = String.valueOf(lng);
        Radius = radius;
        this.types = types;
        //URL GMap Place api
        url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?rankby=distance&keyword=" +
                types +
                "&location=" +
                lat + "," + lng +
                "&key=" + context.getString(R.string.api_key) +
                "&sensor=false&libraries=places";
    }

    public void getMap() throws ExecutionException, InterruptedException {
        if (checkInternetConnection(context)) {
            new GetMapData(context).execute(url);
        }
    }

}
