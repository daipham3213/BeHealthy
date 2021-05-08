package com.fatguy.behealthy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder>{
    private static final String TAG = "DoctorListAdapter";

    private ArrayList<String> mImg;
    private ArrayList<String> mName;
    private ArrayList<String> mSpec;
    private float[] mRate;
    private Context context;

    public DoctorListAdapter(ArrayList<String> mImg, ArrayList<String> mName, ArrayList<String> mSpec, float[] mRate, Context context) {
        this.mImg = mImg;
        this.mName = mName;
        this.mSpec = mSpec;
        this.mRate = mRate;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctor_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");


    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        TextView itemText;
        Spinner itemSpn;
        RelativeLayout parent;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


        }

    }

}
