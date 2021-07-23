package com.sports.oscaracademy.drawerFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.sports.oscaracademy.R;
import com.sports.oscaracademy.databinding.ProfileActivityBinding;

public class profileActivity extends AppCompatActivity {

    private ProfileActivityBinding binding;
    private String userID;

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
        binding = DataBindingUtil.setContentView(this, R.layout.profile_activity);
        userID = getIntent().getStringExtra("UID");

        setSupportActionBar(binding.toolbar.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.toolbar.topTitleName.setText("Student Profile");
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, ProfileFragment.newInstance(userID , getIntent().getStringExtra("editable"))).commit();
    }
}