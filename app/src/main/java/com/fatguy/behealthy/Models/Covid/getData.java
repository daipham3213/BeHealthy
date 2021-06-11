package com.fatguy.behealthy.Models.Covid;

import android.content.Context;

import com.fatguy.behealthy.Models.Utils;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

public class getData {
    private final String Location;
    private final Covid19 data = new Covid19();
    private final Context context;
    private final String[] url = new String[3];

    public getData(@Nullable String location, Context context) {
        Location = location;
        this.context = context;
        if (location == null)
            url[0] = "https://api.canbn.net/api/ncov/Vi%E1%BB%87t%20Nam";
        url[1] = "https://disease.sh/v3/covid-19/countries/vn?strict=true";
        url[2] = "https://disease.sh/v3/covid-19/all";
    }

    public void getStatistic() throws ExecutionException, InterruptedException {
        if (Utils.checkInternetConnection(context)) {
            new GetCovid19(context).execute(url);
        }
    }


}
