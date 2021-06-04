package com.fatguy.behealthy.Models.Covid;

import android.content.Context;

import com.fatguy.behealthy.Models.Utils;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

public class getData {
    private final String Location;
    private final Covid19 data = new Covid19();
    private final Context context;
    private final String url;

    public getData(@Nullable String location, Context context) {
        Location = location;
        this.context = context;
        if (location == null)
            url = "https://api.canbn.net/api/ncov/Vi%E1%BB%87t%20Nam";
        else url = "https://api.canbn.net/api/ncov/" + location;
    }

    public void getStatistic() throws ExecutionException, InterruptedException {
        if (Utils.checkInternetConnection(context)) {
            new GetCovid19(context).execute(url);
        }
    }


}
