package com.sports.oscaracademy.HomeActivities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.ActivityPayAndPlayBinding


class payAndPlay : AppCompatActivity() {

    lateinit var role: String
    lateinit var binding: ActivityPayAndPlayBinding
    private lateinit var navController: NavController
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_pay_and_play)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentHolder) as NavHostFragment
        navController = navHostFragment.navController
        prefs = getSharedPreferences("tokenFile", MODE_PRIVATE)
        role = prefs.getString("userType", "-1")!!

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val prefs = getSharedPreferences("tokenFile", MODE_PRIVATE)
        role = prefs.getString("userType", "-1")!!

        if (role == "-2" && navController.currentDestination?.id == navController.graph.startDestination)
            menuInflater.inflate(R.menu.pay_play_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                if (navController.currentDestination?.id == navController.graph.startDestination) {
                    finish()
                    return true
                }
                navController.navigateUp()
                return true
            }

            R.id.viewBookings -> {
                navController.navigate(
                    R.id.action_pay_play2_to_adminBooking,
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}