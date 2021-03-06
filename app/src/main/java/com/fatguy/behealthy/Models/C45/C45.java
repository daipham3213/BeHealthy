package com.fatguy.behealthy.Models.C45;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.fatguy.behealthy.Activities.Diagnose;
import com.fatguy.behealthy.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C45 extends AsyncTask<Void, Void, Attribute[]> {
    private final Context context;
    private static final String TAG = "C45";
    private ProgressDialog progDailog;
    private final String selection;

    public C45(Context context, String selection) {
        this.context = context;
        this.selection = selection;
    }

    @Override
    public Attribute[] doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: Start loading data...");
        Scanner scan;
        InputStream is = context.getResources().openRawResource(R.raw.training);
        // start loop for all files HERE
        scan = new Scanner(is);
        String headerLine = scan.nextLine();
        String[] headers = headerLine.split(",");

        // class index is assumed to be the last column
        int classIndex = headers.length - 1;
        int numAttributes = headers.length - 1;

        // store data set attributes
        Attribute[] attributes = new Attribute[numAttributes];
        for(int x = 0; x < numAttributes; x++) {
            attributes[x] = new Attribute(headers[x]);
        }

        // for storing classes and class count
        List<String>  classes      = new ArrayList<String>();
        List<Integer> classesCount = new ArrayList<Integer>();

        // store are values into respected attributes
        // along with respected classes
        while(scan.hasNextLine()){
            Val data = null;
            String inLine = scan.nextLine();
            String[] lineData = inLine.split(",");

            // insert class into classes List
            if(classes.isEmpty()){
                classes.add(lineData[classIndex]);
                classesCount.add(classes.indexOf(lineData[classIndex]), 1);
            }
            else{
                if(!classes.contains(lineData[classIndex])){
                    classes.add(lineData[classIndex]);
                    classesCount.add(classes.indexOf(lineData[classIndex]), 1);
                }
                else {
                    classesCount.set(classes.indexOf(lineData[classIndex]),
                            classesCount.get(classes.indexOf(lineData[classIndex])) + 1);
                }
            }

            // insert data into attributes
            for(int x = 0; x < numAttributes; x++){
                data = new Val(lineData[x], lineData[classIndex]);
                attributes[x].insertVal(data);
            }
        }
        int totalNumClasses = 0;
        for(int i : classesCount){
            totalNumClasses += i;
        }
        double IofD = calcIofD(classesCount); // Set information criteria


        for (Attribute a : attributes) {
            a.setGain(IofD, totalNumClasses);
        }
        return attributes;
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
    protected void onPostExecute(Attribute[] attribute) {
        super.onPostExecute(attribute);
        if (progDailog.isShowing()) {
            progDailog.dismiss();
        }
        Log.d(TAG, "onPostExecute: Done");
        Intent start_diag = new Intent(context, Diagnose.class);
        start_diag.putExtra("selection", selection);
        start_diag.putExtra("attrs", attribute);
        context.startActivity(start_diag);
    }

    public static double calcIofD(List<Integer> classesCount){
        double IofD = 0.0;
        double temp = 0.0;

        int totalNumClasses = 0;
        for(int i : classesCount){
            totalNumClasses += i;
        }

        for(double d : classesCount){
            temp = (-1 * (d/totalNumClasses)) * (Math.log((d/totalNumClasses)) / Math.log(2));
            IofD += temp;
        }
        return IofD;
    }
}
