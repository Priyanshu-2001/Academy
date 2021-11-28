package com.sports.oscaracademy.homeActivities.payAndPlayFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.AdminBookingList.outerSlotAdapter
import com.sports.oscaracademy.databinding.FragmentBookingDetailsBinding
import com.sports.oscaracademy.service.GetSlots
import com.sports.oscaracademy.viewModel.AdminBookingFactory
import com.sports.oscaracademy.viewModel.AdminBookingListViewModel

class BookingDetailsViewer : Fragment() {
    lateinit var binding: FragmentBookingDetailsBinding
    val TAG = "BookingDetailsFragment"
    private lateinit var navController: NavController

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

        val doubleBounce: Sprite = WanderingCubes()
        binding.progress.setIndeterminateDrawable(doubleBounce)
        binding.progress.visibility = View.VISIBLE
        (context as AppCompatActivity).setSupportActionBar(binding.materialToolbar)
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getAdminBookingList().observe(viewLifecycleOwner, { data ->
            binding.slots.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            GetSlots().getData().observe(viewLifecycleOwner, Observer {
                binding.slots.adapter = outerSlotAdapter(data, it)
                binding.progress.visibility = View.GONE
            })

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
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