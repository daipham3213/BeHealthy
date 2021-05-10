package com.fatguy.behealthy;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class DiagnoseFragment extends Fragment {

    View root;
    RecyclerView recyclerView;
    private int[] mImg;
    private ArrayList<String> mDes = new ArrayList<>();
    private int[] mContent;
    private Context context;

    private void initData()
    {
        mImg = new int[]{R.drawable.ic_oxygen,R.drawable.ic_hurted_finger_with_bandage,R.drawable.ic_men_silhouette,R.drawable.ic_lungs_silhouette,
                R.drawable.ic_anorexia,R.drawable.ic_anorexia,R.drawable.ic_stomach_shape,R.mipmap.ic_anorexia_w_foreground,R.drawable.ic_pregnancy,R.drawable.ic_stethoscope};
        mDes.add("Do you tired?");
        mDes.add("Do you hurt?");
        mDes.add("Which part of your body?");
        mDes.add("Is it hard to breath?");
        mDes.add("Do you have anorexia?");
        mDes.add("Losing weight?");
        mDes.add("Having a hard time to digesting food?");
        mDes.add("Is your skin color change?");
        mDes.add("You on a pregnancy?");
        mDes.add("Can you categorize your symptoms");
        mContent = new int[]{R.array.diag_tired,R.array.diag_pain,R.array.diag_bodypart,R.array.diag_breathing,R.array.diag_anorexia,R.array.diag_loseweight,
                R.array.diag_digestion,R.array.diag_skin_color,R.array.diag_pregnant,R.array.diag_categorize};
    }

    public DiagnoseFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_diagnose, container, false);
        recyclerView = root.findViewById(R.id.doctor_recycler);
        initData();
        DiagItemAdapter adapter = new DiagItemAdapter(mImg,mDes,mContent,context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}