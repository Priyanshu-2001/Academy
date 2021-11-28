package com.sports.oscaracademy.homeActivities.payAndPlayFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.FragmentAdminBookingBinding
import org.threeten.bp.LocalDate


class AdminBooking : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAdminBookingBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin__booking, container, false)
        binding.calendarView.setSelectedDate(LocalDate.now())
        (context as AppCompatActivity).setSupportActionBar(binding.adminToolbar)
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topTitleName.text = "Booking Details"
        binding.getAttend.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    companion object {
        fun newInstance() = AdminBooking()
    }


    override fun onClick(v: View?) {
        val selectedDate = binding.calendarView.selectedDate
        val bundle = bundleOf("date" to selectedDate?.date)
        selectedDate?.apply {
            navController.navigate(
                R.id.action_adminBooking_to_bookingDetails,
                bundle
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}