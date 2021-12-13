package com.sports.oscaracademy.homeActivities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.razorpay.PaymentResultListener
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.FeesHistoryAdapter
import com.sports.oscaracademy.databinding.ActivityFeesPaymentBinding
import com.sports.oscaracademy.service.PaymentStudentData
import com.sports.oscaracademy.utils.FeesPaymentHelper
import com.sports.oscaracademy.viewModel.FeesViewModel

class FeesPayment : AppCompatActivity(), PaymentResultListener {
    lateinit var binding: ActivityFeesPaymentBinding
    lateinit var viewModel: FeesViewModel
    lateinit var totalFees: String
    lateinit var progressBar: ProgressDialog
    private var CurrentStudentData: PaymentStudentData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fees_payment)
        viewModel = ViewModelProvider(this)[FeesViewModel::class.java]
        progressBar = ProgressDialog(this, R.style.AlertDialog)
        progressBar.setMessage("Confirming Payment")

        viewModel.getSessionDetail()?.observe(this, {
            binding.feesStructure.priceProgressBar.visibility = View.GONE
            binding.feesStructure.totalFees.animate().alpha(1f)
            if (it.fees != "null") {
                binding.feesStructure.totalFees.text = String.format("₹ " + it.fees)
                totalFees = it.fees
            } else {
                binding.feesStructure.totalFees.text = "N/A"
            }
        })

        viewModel.getPaymentStatus()?.observe(this, {
            Log.e("TAG", "onCreate: validity $it")
            if (it != null) {
                if (it.lowercase().trim() == "active") {
                    binding.PayNow.visibility = View.VISIBLE
                    binding.PayNow.text = "Already Paid"
                    binding.PayNow.animate().alpha(1f)
                }
                if (it.lowercase().trim() == "inactive") {
                    binding.PayNow.visibility = View.VISIBLE
                    binding.PayNow.isEnabled = true
                    binding.PayNow.animate().alpha(1f)
                }
            } else {
                val toast = Toast.makeText(
                    this,
                    "Hey Your Fees Details are not ready yet \n    Check out this Section in sometime",
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        })
        binding.PayNow.setOnClickListener {
            progressBar.show()
            viewModel.getFeesPayementObserver().observe(this, {
                if (it) {
                    viewModel.getPaymentStatus()
                    viewModel.getPaymentHistory()
                    progressBar.dismiss()
                } else {
                    progressBar.dismiss()
                    Toast.makeText(
                        this,
                        "Payment Failed please contact Academy For more Details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            if (CurrentStudentData == null) {
                viewModel.getStudentData()?.observe(this, {
                    CurrentStudentData = it
                    Log.e("TAG", "onCreate: payment Called")
                    FeesPaymentHelper().startPayment(
                        this,
                        Integer.valueOf(totalFees),
                        email = it.email,
                        phoneNumber = it.phoneNumber,
                        name = it.name
                    )
                })
            } else {
                FeesPaymentHelper().startPayment(
                    this,
                    Integer.valueOf(totalFees),
                    email = CurrentStudentData!!.email,
                    phoneNumber = CurrentStudentData!!.phoneNumber,
                    name = CurrentStudentData!!.name
                )
            }
        }

        viewModel.getPaymentHistory()?.observe(this, {
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

    override fun onPaymentSuccess(p0: String?) {
        viewModel.startPayment()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        progressBar.dismiss()
    }
}