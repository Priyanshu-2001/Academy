package com.sports.oscaracademy.homeActivities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.ActivityPayAndPlayBinding
import com.sports.oscaracademy.viewModel.Pay_playViewModel


class PayAndPlay : AppCompatActivity(), PaymentResultListener {

    lateinit var role: String
    lateinit var binding: ActivityPayAndPlayBinding
    private lateinit var navController: NavController
    private lateinit var prefs: SharedPreferences
    private lateinit var model: Pay_playViewModel
    lateinit var progressBar: SpinKitView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_pay_and_play)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentHolder) as NavHostFragment
        model = ViewModelProvider(this).get(Pay_playViewModel::class.java)
        Checkout.preload(this)

        progressBar = binding.progress
        val doubleBounce: Sprite = WanderingCubes()
        progressBar.setIndeterminateDrawable(doubleBounce)
        navController = navHostFragment.navController
        prefs = getSharedPreferences("tokenFile", MODE_PRIVATE)
        role = prefs.getString("userType", "-1")!!
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val prefs = getSharedPreferences("tokenFile", MODE_PRIVATE)
        role = prefs.getString("userType", "-1")!!

        if (role == "-2" && navController.currentDestination?.id == navController.graph.startDestinationId)
            menuInflater.inflate(R.menu.pay_play_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                if (navController.currentDestination?.id == navController.graph.startDestinationId) {
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

    override fun onPaymentSuccess(p0: String?) {
        model.payFees(binding.root.rootView, progressBar)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Some Error Occurred while Payment", Toast.LENGTH_LONG).show()
        Log.e("TAG", "onPaymentError: $p1")
    }

}