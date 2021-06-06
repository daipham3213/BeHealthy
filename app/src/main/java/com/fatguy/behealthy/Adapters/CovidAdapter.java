package com.fatguy.behealthy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Models.Covid.Data;
import com.fatguy.behealthy.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CovidAdapter extends RecyclerView.Adapter<CovidAdapter.ViewHolder> {
    @NonNull
    @NotNull

    ArrayList<Data> dataArrayAdapter = new ArrayList<>();
    Context context;
    private final ArrayList<Boolean> is_header = new ArrayList<>();

    public CovidAdapter(Context context) {
        this.context = context;
        Data init = new Data();
        init.setCountry("Country");
        init.setStatus("Status");
        init.setAdds("Address");
        init.setAge("Age");
        init.setName("Name");
        dataArrayAdapter.add(init);
    }

    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CovidAdapter.ViewHolder holder;
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_covid, parent, false);
        holder = new CovidAdapter.ViewHolder(view);
        return holder;
    }

    public void addMessage(Data data) {
        dataArrayAdapter.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String namedata = dataArrayAdapter.get(position).getName();
        String agedata = dataArrayAdapter.get(position).getAge();
        String Addsdata = dataArrayAdapter.get(position).getAdds();
        String Statusdata = dataArrayAdapter.get(position).getStatus();
        String Countrydata = dataArrayAdapter.get(position).getCountry();
        holder.name.setText(namedata);
        holder.age.setText(agedata);
        holder.adds.setText(Addsdata);
        holder.stauts.setText(Statusdata);
        holder.country.setText(Countrydata);

    }

    @Override
    public int getItemCount() {
        return dataArrayAdapter.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{


        private TextView name;
        private TextView age;
        private TextView adds;
        private TextView stauts;
        private TextView country;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.covid_txt_id);
            age = (TextView) itemView.findViewById(R.id.covid_txt_age);
            adds = (TextView) itemView.findViewById(R.id.covid_txt_adds);
            stauts  = (TextView) itemView.findViewById(R.id.covid_txt_status);
            country  = (TextView) itemView.findViewById(R.id.covid_txt_country);
        }
    }
}
