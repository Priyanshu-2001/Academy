package com.sports.oscaracademy.service

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BookingHistoryService {
    val dataList = MutableLiveData<ArrayList<BookingListData>>()
    val TAG = "History"
    val tempListData = ArrayList<BookingListData>()

    fun getListOfBooking(): MutableLiveData<ArrayList<BookingListData>> {
        FirebaseFirestore.getInstance()
            .collection("user")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("Booked Slots")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                it.forEach { doc ->
                    Log.e(TAG, doc.data.toString())
                    doc.apply {
                        val temp = BookingListData(
                            get("name").toString(),
                            getString("totalAmountPaid").toString(),
                            get("email").toString(),
                            get("otp").toString(),
                            getTimestamp("timeStamp")!!,
                            getString("referenceID")!!,
                            get("userID").toString(),
                            get("bookingDate").toString(),
                            get("courtBooked") as HashMap<String, Array<String>>,
                            get("slotsBooked") as HashMap<String, String>,
                            get("phoneNumber").toString()
                        )
                        tempListData.add(temp)
                    }
                }
                dataList.value = tempListData
                Log.e(TAG, "getListOfBooking: dataList = ${dataList.value}")
            }.addOnFailureListener {

            }
        return dataList
    }
}

data class BookingListData(
    val name: String,
    val totalAmountPaid: String,
    val email: String,
    val otp: String,
    val timeStamp: Timestamp,
    val referenceID: String,
    val userID: String,
    val bookingDate: String,
    val courtBooked: HashMap<String, Array<String>>,
    val slotsBooked: HashMap<String, String>,
    val phoneNumber: String
)
