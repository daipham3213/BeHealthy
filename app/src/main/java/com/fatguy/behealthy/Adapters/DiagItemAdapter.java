package com.fatguy.behealthy.Adapters;

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

import com.fatguy.behealthy.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DiagItemAdapter extends RecyclerView.Adapter<DiagItemAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private final int[] mImg;
    private final ArrayList<String> mDes;
    private final int[] mContent;
    private final Context context;

    public DiagItemAdapter(int[] mImg, ArrayList<String> mDes, int[] mContent, Context context) {
        this.mImg = mImg;
        this.mDes = mDes;
        this.mContent = mContent;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.LoadList(mContent[position]);
        holder.itemImg.setImageResource(mImg[position]);
        holder.itemText.setText(mDes.get(position));
    }

    @Override
    public int getItemCount() {
        return mImg.length-1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImg;
        TextView itemText;
        Spinner itemSpn;
        RelativeLayout parent;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_img);
            itemText = itemView.findViewById(R.id.item_txtTitle);
            itemSpn = itemView.findViewById(R.id.item_spnItem);

            parent = itemView.findViewById(R.id.item_rltItem);

        }
        private void LoadList(int resId) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    resId, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemSpn.setAdapter(adapter);
        }
    }

}
