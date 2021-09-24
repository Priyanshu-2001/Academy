package com.sports.oscaracademy.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
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
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookingService {

    val TAG = "BOOKING SERVICE"
    fun BookCourt(
        selectedDate: MutableLiveData<DateTime>,
        selectedslots: MutableLiveData<ArrayList<SlotsData>>,
        bookingData: MutableLiveData<BookingData>
    ) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        val randomKey = FirebaseDatabase.getInstance().reference.push().key
        val data: HashMap<String, Any> = HashMap()
        val otp = (Math.random() * 9000).toInt() + 1000
        bookingData.value?.name?.let { data.put("name", it) }
        data["phoneNumber"] = bookingData.value!!.phoneNumber
        data["email"] = bookingData.value!!.email
        data["userID"] = bookingData.value!!.userID
//        Log.e(TAG, "BookCourt: booking Data " + bookingData.value)
        data["courtID"] = bookingData.value!!.courtID!!
        data["otp"] = otp
        data["referenceID"] = randomKey.toString()

        val date = DateFormater(selectedDate)

        selectedslots.value?.forEach {
            val slotData = it
            firestore.collection("slotBooking").document(date.toString())
                .collection(it.slotID).document(bookingData.value!!.userID)
                .set(data, SetOptions.merge()).addOnSuccessListener {
                    Log.e("TAG", "BookCourt: Booking Successfully")
                    saveRefference(
                        date.toString(),
                        randomKey.toString(),
                        slotData,
                        bookingData,
                        otp
                    )
                }.addOnFailureListener {
                    Log.e("TAG", "BookCourt: " + it.localizedMessage)
                }
        }

    }

    private fun saveRefference(
        selectedDate: String,
        referenceKey: String,
        slotData: SlotsData,
        bookingData: MutableLiveData<BookingData>,
        otp: Int,
    ) {
        //Generate reference ID and Show it to booking User with otp

        val referenceData = HashMap<String, Any>()

        referenceData["referenceID"] = referenceKey
        referenceData["otp"] = otp
        referenceData["userID"] = bookingData.value!!.userID
        Log.e(TAG, "safeRefference: " + selectedDate)

        bookingData.value!!.courtID?.forEach {
            FirebaseDatabase.getInstance().reference.child("BookedSlots")
                .child(selectedDate)
                .child(slotData.slotID)
                .child(it)
                .updateChildren(referenceData)
        }
    }

    fun getAvailableSlots(selectedDate: MutableLiveData<DateTime>) {
        val date = DateFormater(selectedDate)
        var totalBooked: Int
        FirebaseDatabase.getInstance().reference
            .child("BookedSlots")
            .child(date)
            .get().addOnSuccessListener {
                totalBooked = it.childrenCount.toInt()
            }
    }

    fun getAvailableCourts() {

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
        Log.e(TAG, "getTotalCout: " + totalCout)
    }

    fun getAllBookedDATA(date: String): MutableLiveData<ArrayList<BookedDATA>> {
        val bookedSlotsData: ArrayList<BookedDATA> = ArrayList()
        val bookedTotalData: MutableLiveData<ArrayList<BookedDATA>> = MutableLiveData()

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


    fun DateFormater(selectedDate: MutableLiveData<DateTime>): String {
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())

        val date = simpleDateFormat.parse(selectedDate.value.toString())
        return date!!.toString()
    }
}

//
//FirebaseDatabase.getInstance().reference
//.child("BookedSlots")
//.child(date)
//.get().addOnSuccessListener {
//    it.children.forEach {
////                    Log.e(TAG, "getAllBookedDATA: outside" + it )
//        lateinit var tempData: BookedDATA
//        val slotID = it.key.toString()
//        val BookedSlots = it.childrenCount
//        val data: ArrayList<bookedUnitData> = ArrayList()
//
//        it.children.forEach {
////                        Log.e(TAG, "getAllBookedDATA: inner "+ it )
//            val d = bookedUnitData(
//                it.child("otp").value!!,
//                it.child("referenceID").value!!,
//                it.child("userID").value!!
//            )
//            data.add(d)
////                        data.add(it.getValue(bookedUnitData::class.java)!!)
//        }
//        tempData = BookedDATA(slotID, data, it.key!!, BookedSlots)
//        bookedSlotsData.add(tempData)
////                    Log.e(TAG, "getAllBookedDATA: tempData "+ tempData  )
//    }
//    bookedTotalData.postValue(bookedSlotsData)
//}
//return bookedTotalData