package com.sports.oscaracademy.homeActivities.payAndPlayFragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.sports.oscaracademy.R
import com.sports.oscaracademy.databinding.FragmentIndividualBookingDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class IndividualBookingDetails : Fragment(R.layout.fragment_individual_booking_details) {
    var binding: FragmentIndividualBookingDetailsBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIndividualBookingDetailsBinding.bind(view)
        arguments?.apply {
            binding?.apply {
                (context as AppCompatActivity).setSupportActionBar(include2.root)
                (context as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                (context as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
                include2.topTitleName.text = "Booking Details"
                Log.e("TAG", "onViewCreated: " + get("courtData") + get("slotsData"))
                val slotsData =
                    get("slotsData") as HashMap<*, *>//{4=7:00 - 8:00, 6=9:00 - 10:00, 8=11:00 - 12:00}
                val totalSlots = slotsData.size
                var key: String? = null
                slotsData.forEach { it ->
                    key = it.key as String
                }
                if (key != null) {
                    val courtData =
                        get("courtData") as HashMap<*, *> //{4=[1, 2, 3], 6=[1, 2, 3], 8=[1, 2, 3]}
                    val firstSlotTotalCourt = courtData[key] as ArrayList<*>
                    val totalSingleSlotCourtCount = firstSlotTotalCourt.size
                    val totalCourtCount = totalSingleSlotCourtCount * totalSlots
                    ("Court Booked : $totalSingleSlotCourtCount X $totalSlots").also {
                        detailsSection.totalSlots.text = it
                    }
                }
                val timeStamp = get("timeStamp") as Timestamp
//                val sfd = SimpleDateFormat("MMM dd,yyyy" , Locale.getDefault());
                val sfd = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault())
                val formattedData = sfd.format(timeStamp.toDate())
                "Booked On : $formattedData".also { detailsSection.date.text = it }
                ("Name : " + get("name").toString()).also { detailsSection.nametxt.text = it }
                ("Otp : ${get("otp").toString()}").also { detailsSection.otpText.text = it }
                ("Phone Number : ${get("phoneNumber").toString()}").also {
                    detailsSection.phoneNumbere.text = it
                }
                ("Reference ID : ${get("referenceKey").toString()}").also {
                    detailsSection.referenceKey.text = it
                }
                ("₹ ${get("TotalAmountPaid").toString()}").also { amountPaid.text = it }

                val bookingFor = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(get("date") as String)!!
                DateBooked.text = sfd.format(bookingFor)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}