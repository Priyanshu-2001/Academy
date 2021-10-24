package com.sports.oscaracademy.HomeActivities.payAndplayFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.FragmentBookingDetailsBinding
import com.sports.oscaracademy.viewModel.AdminBookingFactory
import com.sports.oscaracademy.viewModel.AdminBookingListViewModel

class BookingDetailsViewer : Fragment() {
    lateinit var binding: FragmentBookingDetailsBinding
    val TAG = "BookingDetailsFragment"
    lateinit var viewModel: AdminBookingListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_booking_details, container, false)
        val selectedDate = arguments?.get("date") ?: CalendarDay.today()

        viewModel = ViewModelProvider(this, AdminBookingFactory(selectedDate.toString())).get(
            AdminBookingListViewModel::class.java
        )

        viewModel.getAdminBookingList().observe(viewLifecycleOwner, {
            Log.e(TAG, "onCreateView: $it")
        })
        return binding.root
    }
}