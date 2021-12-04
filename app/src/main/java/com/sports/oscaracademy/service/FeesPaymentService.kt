package com.sports.oscaracademy.service

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FeesPaymentService {
    private var paymentData = MutableLiveData<List<PaymentData>>()
    private var paymentStatus = MutableLiveData<String>()
    private var paymentStudentData = MutableLiveData<PaymentStudentData>()
    val firestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    fun payNow(sessionDetails: MutableLiveData<SessionData>) {
        val referenceKey = FirebaseDatabase.getInstance().reference.push().key
        val dateFormatter = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("hh:mm,a", Locale.getDefault())
        val date = dateFormatter.format(Date())
        val time = timeFormatter.format(Date())
        val data = HashMap<String, Any>()
        data["amount"] = sessionDetails.value!!.fees
        data["date"] = date
        data["time"] = time
        data["reference"] = referenceKey.toString()
        data["timeStamp"] = Timestamp.now()

        firestore
            .collection("students")
            .document(uid)
            .collection("fees")
            .document() //think of using month-year as document reference over here
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                val mem = HashMap<String, String>()
                mem["membership"] = "Active"
                firestore
                    .collection("students")
                    .document(uid)
                    .set(mem, SetOptions.merge())
            }
    }

    fun getPaymentHistory(): MutableLiveData<List<PaymentData>> {
        firestore
            .collection("students")
            .document(uid)
            .collection("fees")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
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

    fun getMonthlyPaymentStatus(): MutableLiveData<String> {
        firestore
            .collection("students")
            .document(uid)
            .get()
            .addOnSuccessListener {
                paymentStatus.value = it["membership"] as String?
            }
        return paymentStatus
    }

    fun getStudentDataForPayment(): MutableLiveData<PaymentStudentData> {
        firestore
            .collection("students")
            .document(uid)
            .get()
            .addOnSuccessListener {
                paymentStudentData.value = PaymentStudentData(
                    it["name"] as String, it["email"] as String, it["Phone Number"] as String
                )
            }
        return paymentStudentData
    }
}

data class PaymentStudentData(val name: String, val email: String, val phoneNumber: String)

data class PaymentData(
    val amount: String,
    val date: String,
    val reference: String,
    val time: String
)
