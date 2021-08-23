
package com.sports.oscaracademy.HomeActivities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.databinding.ActivityUpcomingTournamentBinding;

public class upcoming_tournament extends AppCompatActivity {

    ActivityUpcomingTournamentBinding binding;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upcoming_tournament);
        setSupportActionBar((Toolbar) binding.include.getRoot());
        binding.include.topTitleName.setText("Upcoming Tournaments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }
}