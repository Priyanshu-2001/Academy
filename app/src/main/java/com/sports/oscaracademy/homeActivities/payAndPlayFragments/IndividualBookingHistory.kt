package com.sports.oscaracademy.homeActivities.payAndPlayFragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import com.sports.oscaracademy.R
import com.sports.oscaracademy.adapters.IndividualBookingListAdapter
import com.sports.oscaracademy.databinding.FragmentIndividualBookingHistoryBinding
import com.sports.oscaracademy.service.BookingListData
import com.sports.oscaracademy.viewModel.PayAndPlayHistoryViewModel

class IndividualBookingHistory : Fragment(R.layout.fragment_individual_booking_history),
    IndividualBookingListAdapter.onListItemClickListener {
    lateinit var finalData: ArrayList<BookingListData>
    var binding: FragmentIndividualBookingHistoryBinding? = null
    lateinit var navController: NavController
    lateinit var viewModel: PayAndPlayHistoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[PayAndPlayHistoryViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIndividualBookingHistoryBinding.bind(view)
        navController = Navigation.findNavController(view)
        val data = viewModel.getBookingHistoryData()
        val thisReference = this
        binding!!.apply {
            val doubleBounce: Sprite = WanderingCubes()
            progressBar.setIndeterminateDrawable(doubleBounce)
            progressBar.visibility = View.VISIBLE
            toolbar.topTitleName.text = "Booking History"
            (context as AppCompatActivity).setSupportActionBar(toolbar.root)
            (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (context as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
            data?.observe(viewLifecycleOwner, { data ->
                finalData = data
                rcvContainer.adapter = IndividualBookingListAdapter(data, thisReference)
                progressBar.visibility = View.GONE
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onItemClicked(position: Int) {
        val bundle = bundleOf(
            Pair("phoneNumber", finalData[position].phoneNumber),
            Pair("otp", finalData[position].otp),
            Pair("referenceKey", finalData[position].referenceID),
            Pair("name", finalData[position].name),
            Pair("date", finalData[position].bookingDate),
            Pair("TotalAmountPaid", finalData[position].totalAmountPaid),
            Pair("slotsData", finalData[position].slotsBooked),
            Pair("courtData", finalData[position].courtBooked),
            Pair("timeStamp", finalData[position].timeStamp)
        )
        navController.navigate(
            R.id.action_individualBookingHistory_to_individualBookingDetails,
            bundle
        )
    }

}