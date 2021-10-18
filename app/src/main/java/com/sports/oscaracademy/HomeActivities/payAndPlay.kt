package com.sports.oscaracademy.HomeActivities

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.sports.oscaracademy.HomeActivities.payAndplayFragments.Admin_Booking
import com.sports.oscaracademy.HomeActivities.payAndplayFragments.pay_play
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.ActivityPayAndPlayBinding


class payAndPlay : AppCompatActivity() {

    lateinit var role: String
    lateinit var binding: ActivityPayAndPlayBinding
    val privateBooking = pay_play.newInstance()
    val adminSection = Admin_Booking.newInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_pay_and_play)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = getSharedPreferences("tokenFile", MODE_PRIVATE)
        role = prefs.getString("userType", "-1")!!

        if (role == "-2") {
            val context = this
            supportFragmentManager.beginTransaction()
                .add(binding.fragmentHolder.id, adminSection, "list").hide(adminSection).commit()

            (binding.topTitleName.layoutParams as ConstraintLayout.LayoutParams).apply {
                marginEnd = 0.dpToPixels(context)
            }
        }

        binding.topTitleName.text = "PAY AND PLAY"
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentHolder.id, privateBooking).commit()
    }

    fun Int.dpToPixels(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val prefs = getSharedPreferences("tokenFile", MODE_PRIVATE)
        role = prefs.getString("userType", "-1")!!

        if (role == "-2")
            menuInflater.inflate(R.menu.pay_play_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager
                        .findFragmentByTag("list")?.isVisible == true
                ) {
                    supportFragmentManager.beginTransaction()
                        .show(privateBooking).hide(adminSection).commit()
                } else {
                    finish()
                }
            }

            R.id.viewBookings -> {
                if (supportFragmentManager
                        .findFragmentByTag("list")?.isVisible == true
                ) {
                    supportFragmentManager.beginTransaction()
                        .show(privateBooking).hide(adminSection).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .show(adminSection).hide(privateBooking).commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager
                .findFragmentByTag("list")?.isVisible == true
        ) {
            supportFragmentManager.beginTransaction()
                .show(privateBooking).hide(adminSection).commit()
        } else {
            finish()
        }
    }

}