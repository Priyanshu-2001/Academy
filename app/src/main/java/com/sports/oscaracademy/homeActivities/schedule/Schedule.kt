package com.sports.oscaracademy.homeActivities.schedule

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.ActivityScheduleBinding
import com.sports.oscaracademy.viewModel.ScheduleViewModel

class Schedule : AppCompatActivity() {
    lateinit var binding: ActivityScheduleBinding
    lateinit var viewModel: ScheduleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule)
        viewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        viewModel.getUserSchedule().observe(this, {
            binding.timings.visibility = View.VISIBLE
            binding.textView8.visibility = View.VISIBLE
            binding.timings.animate().translationY(-200f)
            binding.textView8.animate().translationY(-200f)
            binding.progressBar.visibility = View.GONE
            binding.progressBarBelow.visibility = View.GONE
            binding.session.text = it.session
            binding.timings.text = it.schedule
            if (it.schedule == "null") {
                binding.session.visibility = View.GONE
                binding.textView6.visibility = View.GONE
                binding.textView8.visibility = View.GONE
                binding.timings.visibility = View.GONE
                binding.NoDataImage.visibility = View.VISIBLE
                binding.NoDataText.visibility = View.VISIBLE
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Home -> finish()
        }
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}