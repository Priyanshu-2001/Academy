package com.sports.oscaracademy.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.sports.oscaracademy.data.AdminBookedSlotsData
import com.sports.oscaracademy.data.SingleCourtBookingData
import com.sports.oscaracademy.data.SingleSlotData

class AdminBookingServices {
    fun getAllBookedSlots(date: String): MutableLiveData<AdminBookedSlotsData> {
        val fullData = MutableLiveData<AdminBookedSlotsData>()
        val slotsData = ArrayList<SingleSlotData>()
        val SlotList = ArrayList<String>()
        FirebaseDatabase.getInstance().reference.child("BookedSlots")
            .child(date)
            .get().addOnSuccessListener {
                it.children.forEach { inner -> //Iterating through slots / slotIDs
                    val SingleSlotID = inner.key
                    val BookedCourtIDList = ArrayList<String>()
                    val BookedCourtDataList = ArrayList<SingleCourtBookingData?>()
                    SingleSlotID?.let { id -> SlotList.add(id) }
                    inner.children.forEach { courtDataList -> //iterating through courtIDs in slots
                        courtDataList.key?.let { it1 -> BookedCourtIDList.add(it1) }
                        val bookingDetails =
                            courtDataList.getValue(SingleCourtBookingData::class.java)
                        BookedCourtDataList.add(bookingDetails)
                    }
                    slotsData.add(SingleSlotID?.let { it1 ->
                        SingleSlotData(
                            it1,
                            BookedCourtIDList,
                            BookedCourtDataList
                        )
                    }!!)
                }
                val temp = AdminBookedSlotsData(SlotList, slotsData)
                Log.e(TAG, "getAllBookedSlots: $temp $date")
                fullData.postValue(temp)
            }
        return fullData
    }

    companion object {
        const val TAG = "AdminBookingDetails"
    }
}