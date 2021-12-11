package com.sports.oscaracademy.service

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sports.oscaracademy.data.BookedDATA
import com.sports.oscaracademy.data.BookingData
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.data.bookedUnitData
import com.sports.oscaracademy.homeActivities.payAndPlayFragments.BookingSuccessActivity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookingService {

    companion object {
        fun DateFormater(selectedDate: MutableLiveData<Date>): String {
            val pattern = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())

            val date = simpleDateFormat.parse(selectedDate.value.toString())
            return date!!.toString()
        }
    }

    val TAG = "BOOKING SERVICE"
    fun bookCourt(
        selectedDate: MutableLiveData<Date>,
        selectedslots: MutableLiveData<ArrayList<SlotsData>>,
        bookingData: MutableLiveData<BookingData>,
        progress: SpinKitView,
        checkOutPrice: CharSequence
    ) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val randomKey = FirebaseDatabase.getInstance().reference.push().key
        val data: HashMap<String, Any> = HashMap()
        val otp = (Math.random() * 9000).toInt() + 1000
        bookingData.value?.name?.let { data.put("name", it) }
        data["phoneNumber"] = bookingData.value!!.phoneNumber
        data["email"] = bookingData.value!!.email
        data["userID"] = bookingData.value!!.userID
        data["otp"] = otp
        data["referenceID"] = randomKey.toString()

        val d = selectedDate.value
        val formatter = DecimalFormat("00")
        val mon: String = formatter.format(d?.month?.plus(1)?.toLong())
        val day: String = formatter.format(d?.date?.toLong())
        val date = d?.year?.plus(1900).toString() + "-" + mon + "-" + day
//        val date = tempDateFormat


        val individualBookingData = data
        individualBookingData["timeStamp"] = Timestamp.now()
        val slotMap = HashMap<String, String>()
        val courtMap = bookingData.value!!.courtID!!
        selectedslots.value?.forEach {
            slotMap[it.slotID] = it.slot
        }

        individualBookingData["slotsBooked"] = slotMap
        individualBookingData["courtBooked"] = courtMap
        individualBookingData["bookingDate"] = date
        individualBookingData["totalAmountPaid"] = checkOutPrice

        val db = firestore.collection("user")
            .document(bookingData.value!!.userID)
            .collection("Booked Slots")
            .document()
        db.set(individualBookingData, SetOptions.merge())

//        selectedslots.value?.forEach { selectedSlot ->
//            val tempList = bookingData.value!!.courtID?.get(selectedSlot.slotID)
//            val nonNestedList = ArrayList<String>()
//
////            val db = firestore.collection("slotBooking").document(date.toString())
////                .collection(selectedSlot.slotID).document(bookingData.value!!.userID)
//            val db = firestore.collection("user")
//                .document(bookingData.value!!.userID)
//                .collection("Booked Slots")
//                .document(date)
//                .collection(selectedSlot.slotID)
//                .document()
//
//            val dataCourtIDs: HashMap<String, Any> = HashMap()
//            tempList?.forEach { courtID ->
//                nonNestedList.add(courtID)
//                db.get().addOnSuccessListener {
//                    dataCourtIDs["courtID"] = nonNestedList
//                    if (it["courtID"] != null) {
//                        Log.e(TAG, "BookCourt: $courtID")
//                        db.update("courtID", FieldValue.arrayUnion(courtID))
//                    } else {
//                        db.set(dataCourtIDs, SetOptions.merge())
//                    }
//                }
//            }
//
//            db.set(data, SetOptions.merge()).addOnSuccessListener {
//                Log.e("TAG", "BookCourt: Booking Successfully")
//            }.addOnFailureListener {
//                Log.e("TAG", "BookCourt: " + it.localizedMessage)
//            }
//        }
        saveRefference(
            date,
            randomKey.toString(),
            bookingData,
            otp,
            progress
        )

    }

    private fun saveRefference(
        selectedDate: String,
        referenceKey: String,
        bookingData: MutableLiveData<BookingData>,
        otp: Int,
        progress: SpinKitView,
    ) {
        //Generate reference ID and Show it to booking User with otp
        val totalSlots = bookingData.value?.courtID?.size

        val referenceData = HashMap<String, Any>()
        var successCount = 0
        referenceData["referenceID"] = referenceKey
        referenceData["otp"] = otp
        referenceData["userID"] = bookingData.value!!.userID
        referenceData["name"] = bookingData.value!!.name.toString()
        referenceData["PhoneNumber"] = bookingData.value!!.phoneNumber
        Log.e(TAG, "safeReference: $selectedDate")

        bookingData.value!!.courtID?.forEach {
            it.value.forEach { courtID ->
                FirebaseDatabase.getInstance().reference.child("BookedSlots")
                    .child(selectedDate)
                    .child(it.key)
                    .child(courtID) // arraylist of courtIDs
                    .updateChildren(referenceData)
                    .addOnSuccessListener {
                        successCount++
                        if (successCount == totalSlots) {
                            Toast.makeText(
                                progress.context,
                                "Congrats Your Booking is Successfully Accepted",
                                Toast.LENGTH_SHORT
                            ).show()

                            (progress.context as Activity).finish()
                            val intent = Intent(
                                progress.context,
                                BookingSuccessActivity::class.java
                            )
                            intent.putExtra("phoneNumber", bookingData.value!!.phoneNumber)
                            intent.putExtra("otp", otp)
                            intent.putExtra("referenceKey", referenceKey)
                            intent.putExtra("name", bookingData.value!!.name.toString())
                            intent.putExtra("date", selectedDate)
                            intent.putExtra("totalSlots", totalSlots)
                            progress.context.startActivity(
                                intent
                            )
                        } else {

                        }
                    }
            }
            progress.visibility = View.GONE
        }
//        Log.e(TAG, "saveRefference: $successCount $totalSlots")

    }

    fun getTotalCount(): MutableLiveData<String> {
        val totalCout: MutableLiveData<String> = MutableLiveData()
        FirebaseDatabase.getInstance().reference.get()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    totalCout.value = it.result.child("totalCourts").value.toString()
                    Log.e(TAG, "getTotalCount inside: ${totalCout.value}")
                }
            })
        Log.e(TAG, "getTotalCount: ${totalCout.value}")
        return totalCout
    }

    fun getAllBookedDATA(date: String): MutableLiveData<ArrayList<BookedDATA>> {
        val bookedSlotsData: ArrayList<BookedDATA> = ArrayList()
        val bookedTotalData: MutableLiveData<ArrayList<BookedDATA>> = MutableLiveData()
        Log.e(TAG, "getAllBookedDATA: fate = $date")
        FirebaseDatabase.getInstance().reference
            .child("BookedSlots")
            .child(date)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(it: DataSnapshot) {
                    it.children.forEach {
//                    Log.e(TAG, "getAllBookedDATA: outside" + it )
                        lateinit var tempData: BookedDATA
                        val slotID = it.key.toString()
                        val BookedSlots = it.childrenCount
                        val data: ArrayList<bookedUnitData> = ArrayList()

                        it.children.forEach {
//                        Log.e(TAG, "getAllBookedDATA: inner "+ it )
                            val d = bookedUnitData(
                                it.child("otp").value!!,
                                it.child("referenceID").value!!,
                                it.child("userID").value!!
                            )
                            data.add(d)
//                        data.add(it.getValue(bookedUnitData::class.java)!!)
                        }
                        tempData = BookedDATA(slotID, data, it.key!!, BookedSlots)
                        bookedSlotsData.add(tempData)
//                    Log.e(TAG, "getAllBookedDATA: tempData "+ tempData  )
                    }
                    bookedTotalData.postValue(bookedSlotsData)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        return bookedTotalData
    }


}