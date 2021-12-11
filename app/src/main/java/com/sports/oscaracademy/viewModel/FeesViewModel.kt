package com.sports.oscaracademy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sports.oscaracademy.service.*

class FeesViewModel(val app: Application) : AndroidViewModel(app) {
    private val sessionService = SessionDetailsService()
    private val feesPaymentService = FeesPaymentService()
    private var sessionDetails: MutableLiveData<SessionData>? = null
    private var paymentHistoryData: MutableLiveData<List<PaymentData>>? = null
    private var paymentStatus: MutableLiveData<String>? = null
    private var studentData: MutableLiveData<PaymentStudentData>? = null

    fun getSessionDetail(): MutableLiveData<SessionData>? {
        if (sessionDetails == null)
            sessionDetails = sessionService.getSpecificSchedule(app.applicationContext)
        return sessionDetails
    }

    fun startPayment() {
        feesPaymentService.payNow(sessionDetails!!)
    }

    fun getFeesPayementObserver(): MutableLiveData<Boolean> {
        return feesPaymentService.feesPaymentObserver
    }

    fun getPaymentHistory(): MutableLiveData<List<PaymentData>>? {
        if (paymentHistoryData == null)
            paymentHistoryData = feesPaymentService.getPaymentHistory()
        return paymentHistoryData
    }

    fun getPaymentStatus(): MutableLiveData<String>? {
        if (paymentStatus == null)
            paymentStatus = feesPaymentService.getMonthlyPaymentStatus()
        return paymentStatus
    }

    fun getStudentData(): MutableLiveData<PaymentStudentData>? {
        if (studentData == null)
            studentData = feesPaymentService.getStudentDataForPayment()
        return studentData
    }
}