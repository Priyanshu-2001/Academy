package com.sports.oscaracademy.homeActivities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.FeesHistoryAdapter
import com.sports.oscaracademy.databinding.ActivityFeesPaymentBinding
import com.sports.oscaracademy.viewModel.FeesViewModel

class FeesPayment : AppCompatActivity() {
    lateinit var binding: ActivityFeesPaymentBinding
    lateinit var viewModel: FeesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fees_payment)
        viewModel = ViewModelProvider(this)[FeesViewModel::class.java]

        viewModel.getSessionDetail().observe(this, {
            binding.feesStructure.totalFees.text = String.format("₹ " + it.fees)
        })

        binding.PayNow.setOnClickListener {
            viewModel.startPayment()
        }

        viewModel.getPaymentHistory().observe(this, {
            val adapter = FeesHistoryAdapter(it)
            binding.bottomSheet.paymentRcv.adapter = adapter
        })

        initialAnimation() // rotation animation for arrow

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)

        window.statusBarColor = resources.getColor(R.color.colorPrimary, theme)

        binding.bottomSheet.arrow.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                setOppositeState(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

    }

    private fun initialAnimation() {
        binding.bottomSheet.arrow.animate().apply {
            rotation(-540f)
            duration = 2500
            interpolator = AccelerateDecelerateInterpolator()
        }
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

    private fun setOppositeState(newState: Int) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
            binding.bottomSheet.arrow.animate().apply {
                rotation(-180f)
                duration = 500
                interpolator = AccelerateInterpolator()
            }
        } else {
            binding.bottomSheet.arrow.animate().apply {
                rotation(0f)
                duration = 500
                interpolator = AccelerateInterpolator()
            }
        }
    }
}