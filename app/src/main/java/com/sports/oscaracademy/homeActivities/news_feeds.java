
package com.sports.oscaracademy.homeActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sports.oscaracademy.Dashboard;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.adapters.newsFeedAdapter;
import com.sports.oscaracademy.bottomSheet.newsFeed_b_sheet;
import com.sports.oscaracademy.data.feedsData;
import com.sports.oscaracademy.databinding.NewsFeedsBinding;
import com.sports.oscaracademy.service.feedsService;
import com.sports.oscaracademy.viewModel.feedsViewModel;

public class news_feeds extends AppCompatActivity {

    public ProgressBar progressBar;
    NewsFeedsBinding binding;
    String role;
    SharedPreferences pref;
    feedsViewModel viewModel;
    feedsService service = new feedsService();
    feedsData data;
    newsFeedAdapter adapter;
    String TAG = "newsFeedAct";
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isTaskRoot()) {
                startActivity(new Intent(this, Dashboard.class));
                finish();
            } else {
                super.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(this, Dashboard.class));
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.news_feeds);
        viewModel = new ViewModelProvider(this).get(feedsViewModel.class);
        binding.setLifecycleOwner(this);
        setSupportActionBar(binding.include.getRoot());
        binding.include.topTitleName.setText("Feeds");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar = binding.progress;
        Sprite doubleBounce = new WanderingCubes();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
        pref = getSharedPreferences("tokenFile", MODE_PRIVATE);
        role = pref.getString("userType", "-1");
        Log.e(TAG, "onCreate: " + role);
        adapter = new newsFeedAdapter(this);

        LinearLayoutManager LLmanager = new LinearLayoutManager(this);
        viewModel.getData().observe(this, feedsData -> {
            Log.e("TAG", "onChanged: " + feedsData);
            adapter.setNewData(feedsData);
            binding.recyclerView.setLayoutManager(LLmanager);
            binding.recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            Log.e("TAG", "onChanged: ");
            if (role.equals("-2")) {
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int pos = viewHolder.getAdapterPosition();
                        data = adapter.getDataAt(pos);

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(news_feeds.this, R.style.AlertDialog);
                        adapter.HoldDelete(pos);
                        dialogBuilder.setTitle("Confirm Deletion").setPositiveButton("Sure", (dialog, which) -> {
//                                adapter.notifyItemRemoved(pos);
                            service.delete_feeds(data);
                            showUndoSnackbar(adapter);
                            Log.e("TAG", "onSwiped: count on +ve btn" + adapter.getItemCount());
                        }).setNegativeButton("Cancel", (dialog, which) -> {
                            adapter.addDeleted(pos, data);
                            Log.e("TAG", "onSwiped: count on canceled" + adapter.getItemCount());
                        }).setMessage("Are Sure u wanna Delete this Feed ?").show();
                    }
                }).attachToRecyclerView(binding.recyclerView);
            }
        });
        if (role.equals("-2")) //admin
            binding.fab.setVisibility(View.VISIBLE);
        else
            binding.fab.setVisibility(View.GONE);


        binding.fab.setOnClickListener(v -> {
            newsFeed_b_sheet sheet = new newsFeed_b_sheet();
            sheet.show(getSupportFragmentManager(), "bottomSheet");
        });

    }

    public void showUndoSnackbar(newsFeedAdapter adapter) {
        View view = binding.getRoot().findViewById(R.id.mainLayout);
        Log.e("TAG", "showUndoSnackbar: " + "snackbar is shown ");
        View rootView = this.getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(rootView, R.string.deleted_sucessfully, Snackbar.LENGTH_LONG);
        snackbar.setDuration(10000);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.setBackgroundTint(Color.BLACK);
        snackbar.setAction("UNDO", v -> adapter.undoDelete());
        snackbar.show();
    }

}