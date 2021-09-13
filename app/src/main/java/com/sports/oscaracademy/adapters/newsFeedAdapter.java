package com.sports.oscaracademy.adapters;

import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.data.feedsData;
import com.sports.oscaracademy.service.feedsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class newsFeedAdapter extends RecyclerView.Adapter<newsFeedAdapter.newHolder> {

    ArrayList<feedsData> data;
    String role;
    int currentExpansion = -1;
    feedsService service = new feedsService();
    feedsData mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public newsFeedAdapter(String r) {
        role = r;
    }

    @NonNull
    @Override
    public newHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_upcoming_tournament, parent, false);
        return new newHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull newHolder holder, int position) {
        holder.textView.setText(data.get(position).getFeed());
        Date d = data.get(position).getDate();
        SimpleDateFormat sdftime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        holder.date.setText(sdftime.format(d) + "\n" + sdfDate.format(d));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public feedsData getDataAt(int position) {
        mRecentlyDeletedItemPosition = position;
        mRecentlyDeletedItem = data.get(position);
        return mRecentlyDeletedItem;
    }

    public void undoDelete() {
        data.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        (new Handler()).postDelayed(() -> {
            service.UpdateFeeds(mRecentlyDeletedItem);
        }, 1000);
    }

    public void HoldDelete(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addDeleted(int pos, feedsData data) {
        this.data.add(pos, data);
        Log.e("TAG", "addDeleted: " + data.getFeed());
        notifyItemInserted(pos);
    }

    public void setNewData(ArrayList<feedsData> feedsData) {
        data = feedsData;
        notifyDataSetChanged();
    }

    class newHolder extends RecyclerView.ViewHolder {
        TextView textView, date;

        public newHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.liveFeeds);
            date = itemView.findViewById(R.id.date);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
