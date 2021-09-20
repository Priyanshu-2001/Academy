package com.sports.oscaracademy.HomeActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sports.oscaracademy.HomeActivities.payAndplayFragments.pay_play
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.ActivityPayAndPlayBinding

class payAndPlay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPayAndPlayBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_pay_and_play)
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentHolder.id, pay_play.newInstance()).commit()
    }
}