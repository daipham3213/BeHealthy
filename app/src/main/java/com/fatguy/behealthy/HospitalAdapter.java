package com.fatguy.behealthy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder>{

    private Context context;
    private ArrayList <String> name;
    private ArrayList <String> address;
    private ArrayList <String> status;
    private ArrayList <Double> rate;

    private static final String TAG = "RecyclerViewAdapter";

    public HospitalAdapter(Context context, ArrayList<String> name, ArrayList<String> address, ArrayList<String> status, ArrayList<Double> rate) {
        this.context = context;
        this.name = name;
        this.address = address;
        this.status = status;
        this.rate = rate;
    }

    @NonNull
    @NotNull
    @Override
    public HospitalAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hospital_list,parent,false);
        HospitalAdapter.ViewHolder holder = new HospitalAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.Name.setText(name.get(position));
        holder.Address.setText(address.get(position));
        holder.Status.setText(status.get(position));
        holder.Rate.setText(rate.get(position).toString());
    }


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Address;
        TextView Rate;
        TextView Status;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.list_hospital_name);
            Address = itemView.findViewById(R.id.hospital_address);
            Rate = itemView.findViewById(R.id.hospital_rate);
            Status = itemView.findViewById(R.id.hospital_business_status);
        }
    }
}
