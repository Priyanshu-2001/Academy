package com.sports.oscaracademy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.Message;
import com.sports.oscaracademy.databinding.ItemReceiveBinding;
import com.sports.oscaracademy.databinding.ItemSentBinding;

import java.util.ArrayList;

public class chatMessageAdapter extends RecyclerView.Adapter {
    ArrayList<Message> messages;
    int ITEM_SENT = 1;
    int ITEM_RECEIVE = 2;

    Context context;
    String senderRoom;
    String receiverRoom;

    public chatMessageAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
//        remoteConfig = FirebaseRemoteConfig.getInstance();
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
//        FirebaseDatabase.getInstance().getReference()
//                .child("chats")
//                .child(senderRoom)
//                .child("messages")
//                .child(message.getMessageId()).setValue(message);
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("chats")
//                .child(receiverRoom)
//                .child("messages")
//                .child(message.getMessageId()).setValue(message);

        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSentBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }
}
