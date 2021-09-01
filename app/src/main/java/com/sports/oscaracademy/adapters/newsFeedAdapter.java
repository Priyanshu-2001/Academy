package com.sports.oscaracademy.adapters;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
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

    public newsFeedAdapter(ArrayList<feedsData> d, String r) {
        data = d;
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
        Date d = new Date(data.get(position).getDate());
        SimpleDateFormat sdftime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        holder.date.setText(sdftime.format(d) + "\n" + sdfDate.format(d));

        holder.deletebtn.setOnClickListener(v -> {
            service.delete_feeds(data.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class newHolder extends RecyclerView.ViewHolder {
        Button editbtn, deletebtn;
        TextView textView, date;
        LinearLayoutCompat ll;

        public newHolder(@NonNull View itemView) {
            super(itemView);
            deletebtn = itemView.findViewById(R.id.delete_btn);
            textView = itemView.findViewById(R.id.liveFeeds);
            date = itemView.findViewById(R.id.date);
            ll = itemView.findViewById(R.id.adminPanel);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setOnClickListener(v -> {
                if (role.equals("-2")) {
                    if (ll.getVisibility() == View.GONE) {
                        ll.setVisibility(View.VISIBLE);
                        if (currentExpansion != -1) {

                            ll.setVisibility(View.GONE);
                            currentExpansion = getAdapterPosition();
                        }
                    } else {
                        ll.setVisibility(View.GONE);
                        currentExpansion = -1;
                    }
                }
            });
        }
    }
}
