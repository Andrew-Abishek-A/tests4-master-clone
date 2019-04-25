package com.example.tests4;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMesaageViewHolder.ChatMessageViewHolder> {
    private static final String TAG = "ChatMessageAdapter";
    private final Activity activity;
    List<ChatMessage> messages = new ArrayList<>();

    public ChatMessageAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addMessage(ChatMessage chat) {
        messages.add(chat);
        notifyItemInserted(messages.size());
    }


    @Override
    public ChatMesaageViewHolder.ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatMesaageViewHolder.ChatMessageViewHolder ob = new ChatMesaageViewHolder.ChatMessageViewHolder(activity, activity.getLayoutInflater().inflate(android.R.layout.two_line_list_item, parent, false));
        return ob;
    }

    @Override
    public void onBindViewHolder(ChatMesaageViewHolder.ChatMessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
