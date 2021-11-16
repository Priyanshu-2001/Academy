package com.sports.oscaracademy.HomeActivities.payAndPlayFragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.ActivityBookingSuccesBinding

class BookingSuccessActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookingSuccesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_succes)
        try {
            intent.extras?.apply {
                Log.e(TAG, "onCreate: ${this.get("phoneNumber")}")
                binding.phoneNumbere.text =
                    String.format("Phone Number : ${this.get("phoneNumber")}")
                binding.otpText.text = String.format("OTP : ${this.get("otp")}")
                binding.referenceKey.text =
                    String.format("Reference ID ${this.get("referenceKey")}")
                binding.nametxt.text = String.format("Name : ${this.get("name")}")
                binding.date.text = String.format("Booking Date : ${this.get("date")}")
                binding.totalSlots.text =
                    String.format("Total Slots Booked : ${this.get("totalSlots")}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: ${e.printStackTrace()}")
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
        binding.homeBtn.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TAG = "BookingSuccessful"
    }
}