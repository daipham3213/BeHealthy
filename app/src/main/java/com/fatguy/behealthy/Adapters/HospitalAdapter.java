package com.fatguy.behealthy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Activities.AcitivityGMap;
import com.fatguy.behealthy.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> name;
    private final ArrayList<String> address;
    private final ArrayList<String> status;
    private final ArrayList<Double> rate;
    private final ArrayList<Double> lat;
    private final ArrayList<Double> lng ;
    static AcitivityGMap gmap;
    private int pos;

    private static final String TAG = "RecyclerViewAdapter";

    public HospitalAdapter(Context context, ArrayList<String> name, ArrayList<String> address, ArrayList<String> status, ArrayList<Double> rate, ArrayList<Double> lat, ArrayList<Double> lng) {
        this.context = context;
        this.name = name;
        this.address = address;
        this.status = status;
        this.rate = rate;
        this.lat = lat;
        this.lng = lng;
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
        holder.Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                ViewHolder vh = new ViewHolder(holder.itemView);
                vh.show(pos);
            }
        });

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
        CardView Map;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.list_hospital_name);
            Address = itemView.findViewById(R.id.hospital_address);
            Rate = itemView.findViewById(R.id.hospital_rate);
            Status = itemView.findViewById(R.id.hospital_business_status);
            Map = itemView.findViewById(R.id.hospital_btnMap);

        }

        public void show (int pos){
            gmap = new AcitivityGMap(name.get(pos),address.get(pos), rate.get(pos), status.get(pos),lat.get(pos),lng.get(pos));
            itemView.getContext().startActivity(new Intent(itemView.getContext(), gmap.getClass()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
