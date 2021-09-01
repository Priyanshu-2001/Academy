
package com.sports.oscaracademy.HomeActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.newsFeedAdapter;
import com.sports.oscaracademy.bottomSheet.newsFeed_b_sheet;
import com.sports.oscaracademy.data.feedsData;
import com.sports.oscaracademy.databinding.NewsFeedsBinding;
import com.sports.oscaracademy.viewModel.feedsViewModel;

import java.util.ArrayList;

public class news_feeds extends AppCompatActivity {

    public ProgressBar progressBar;
    NewsFeedsBinding binding;
    String role;
    SharedPreferences pref;
    feedsViewModel viewModel;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.news_feeds);
        viewModel = new ViewModelProvider(this).get(feedsViewModel.class);
        binding.setLifecycleOwner(this);
        setSupportActionBar((Toolbar) binding.include.getRoot());
        binding.include.topTitleName.setText("Feeds");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = binding.progress;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.VISIBLE);
        pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        role = pref.getString("userType", "-1");
        LinearLayoutManager LLmanager = new LinearLayoutManager(this);
        viewModel.getData().observeForever(new Observer<ArrayList<feedsData>>() {
            @Override
            public void onChanged(ArrayList<feedsData> feedsData) {
                Log.e("TAG", "onChanged: " + feedsData);
                newsFeedAdapter adapter = new newsFeedAdapter(feedsData, role);
                binding.recyclerView.setLayoutManager(LLmanager);
                binding.recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
        });
        if (role.equals("-2")) //admin
            binding.fab.setVisibility(View.VISIBLE);


        binding.fab.setOnClickListener(v -> {
            newsFeed_b_sheet sheet = new newsFeed_b_sheet();
            sheet.show(getSupportFragmentManager(), "bottomSheet");
        });
    }
}