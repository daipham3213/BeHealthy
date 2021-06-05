package com.fatguy.behealthy.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatguy.behealthy.Models.Utils;
import com.fatguy.behealthy.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final ArrayList<String> timestamps = new ArrayList<>();
    private final ArrayList<String> messages = new ArrayList<>();
    private final ArrayList<Boolean> is_bot = new ArrayList<>();
    private final String greetings = "Hello there! I'm here to help you!";
    private int type = 0;

    public MessageAdapter() {
        this.is_bot.add(true);
        this.timestamps.add(Utils.dateFormat(1));
        this.messages.add(greetings);
    }

    public MessageAdapter(ArrayList<String> timestamps, ArrayList<String> messages, ArrayList<Boolean> is_bot) {
        this.is_bot.add(true);
        this.timestamps.add(Utils.dateFormat(1));
        this.messages.add(greetings);
        this.timestamps.addAll(timestamps);
        this.messages.addAll(messages);
        this.is_bot.addAll(is_bot);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1 - a user chat
        if (is_bot.get(position)) viewType = 0; //if zero, it will be a bot chat
        this.type = viewType;
        return viewType;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MessageAdapter.ViewHolder holder;
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_botchat, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_userchat, parent, false);
        }
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.ViewHolder holder, int position) {
        holder.mess.setText(messages.get(position));
        holder.timestamp.setText(timestamps.get(position));
    }

    @Override
    public int getItemCount() {
        return timestamps.size();
    }

    public void addMessage(String message, String timestamp, boolean is_bot) {
        messages.add(message);
        timestamps.add(timestamp);
        this.is_bot.add(is_bot);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mess;
        public TextView timestamp;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            if (type == 0) {
                mess = itemView.findViewById(R.id.chat_bot_message);
                timestamp = itemView.findViewById(R.id.chat_bot_timestamp);
            }
            if (type == 1) {
                mess = itemView.findViewById(R.id.chat_user_message);
                timestamp = itemView.findViewById(R.id.chat_user_timestamp);
            }
        }
    }
}
