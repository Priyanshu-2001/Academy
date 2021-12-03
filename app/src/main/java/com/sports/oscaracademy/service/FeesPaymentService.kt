package com.sports.oscaracademy.service

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FeesPaymentService {
    var paymentData = MutableLiveData<List<PaymentData>>()
    val firestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    fun payNow(sessionDetails: MutableLiveData<SessionData>) {
        val referenceKey = FirebaseDatabase.getInstance().reference.push().key
        val dateFormatter = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val MonthYearFormatter = SimpleDateFormat("M-yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = dateFormatter.format(Date())
        val monthYear = MonthYearFormatter.format(Date())
        val time = timeFormatter.format(Date())
        val data = HashMap<String, String>()
        data["amount"] = sessionDetails.value!!.fees
        data["date"] = date
        data["time"] = time
        data["reference"] = referenceKey.toString()

        firestore
            .collection("students")
            .document(uid)
            .collection("fees")
            .document()//think of using month-year as document reference over here
            .set(data, SetOptions.merge())
    }

    fun getPaymentHistory(): MutableLiveData<List<PaymentData>> {
        firestore
            .collection("students")
            .document(uid)
            .collection("fees")
            .get()
            .addOnSuccessListener { dataSnap ->
                val tempData = mutableListOf<PaymentData>()
                dataSnap.documents.forEach {
                    val data = it.data
                    data?.let { newData ->
                        tempData.add(
                            PaymentData(
                                amount = newData["amount"] as String,
                                date = newData["date"] as String,
                                time = newData["time"] as String,
                                reference = newData["reference"] as String
                            )
                        )
                    }
                }
                paymentData.value = tempData
            }
        return paymentData
    }

}

data class PaymentData(
    val amount: String,
    val date: String,
    val reference: String,
    val time: String
)
