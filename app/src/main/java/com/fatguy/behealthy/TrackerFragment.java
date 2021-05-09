package com.fatguy.behealthy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class TrackerFragment extends Fragment {
    private View root;
    private PieChart step_chart;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tracker, container, false);
        step_chart = root.findViewById(R.id.tracker_weekly_chart);
        //Add value to the chart (steps)
        int target = 1000;
        int counted  = 800;
        addValue_pie(step_chart,target, counted);
        return root;
    }

    private void addValue_pie(PieChart step_chart, int target, int counted) {
        int left;
        if (target-counted<0) left = 0; else  left =target-counted;
        PieDataSet  lds = new PieDataSet(step_values(left,counted),"");
        PieData step_data = new PieData(lds);

        //Customize pie chart
        int color[] = {R.color.md_teal_100,R.color.md_blue_grey_100};
        lds.setColors(color);
        String tg = String.valueOf(counted);
        step_chart.setCenterText(tg);
        step_chart.setCenterTextSize(20);
        step_chart.setDrawEntryLabels(false);
        step_chart.setData(step_data);
        step_chart.setHoleRadius(75);
        step_chart.setTransparentCircleRadius(80);
        step_chart.setUsePercentValues(true);
        step_chart.setDescription(null);
        step_chart.invalidate();
    }

    public TrackerFragment() {
    }


    private ArrayList<PieEntry> step_values(float target, float counted)
    {
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(target,"Target"));
        data.add(new PieEntry(counted, "Counted"));
        return data;
    }
}