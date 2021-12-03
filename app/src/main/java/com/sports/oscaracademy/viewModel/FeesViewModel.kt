package com.sports.oscaracademy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sports.oscaracademy.service.FeesPaymentService
import com.sports.oscaracademy.service.PaymentData
import com.sports.oscaracademy.service.SessionData
import com.sports.oscaracademy.service.SessionDetailsService

class FeesViewModel(val app: Application) : AndroidViewModel(app) {
    private val sessionService = SessionDetailsService()
    private val feesPaymentService = FeesPaymentService()
    private var sessionDetails = MutableLiveData<SessionData>()

    fun getSessionDetail(): MutableLiveData<SessionData> {
        sessionDetails = sessionService.getSpecificSchedule(app.applicationContext)
        return sessionDetails
    }

    fun startPayment() {
        feesPaymentService.payNow(sessionDetails)
    }

    fun getPaymentHistory(): MutableLiveData<List<PaymentData>> {
        return feesPaymentService.getPaymentHistory()
    }
}