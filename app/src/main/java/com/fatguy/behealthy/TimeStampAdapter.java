package com.fatguy.behealthy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TimeStampAdapter extends RecyclerView.Adapter<TimeStampAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Integer> hour;
    private ArrayList <Integer> min;
    private ArrayList <String> cups;
    private static final String TAG = "RecyclerViewAdapter";

    public TimeStampAdapter(Context context, ArrayList<Integer> hour, ArrayList<Integer> min, ArrayList <String> cups) {
        this.context = context;
        this.hour = hour;
        this.min = min;
        this.cups = cups;
    }

    @NonNull
    @NotNull
    @Override
    public TimeStampAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_timestamp,parent,false);
        TimeStampAdapter.ViewHolder holder = new TimeStampAdapter.ViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull @NotNull TimeStampAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.h.setText(hour.get(position).toString());
        holder.m.setText(min.get(position).toString());
        holder.a.setText(cups.get(position) + " ml");
    }

    @Override
    public int getItemCount() {
        return hour.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView h;
        TextView m;
        TextView a;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            h = itemView.findViewById(R.id.time_hour);
            m = itemView.findViewById(R.id.time_min);
            a = itemView.findViewById(R.id.time_txtAmount);
        }
    }
}
