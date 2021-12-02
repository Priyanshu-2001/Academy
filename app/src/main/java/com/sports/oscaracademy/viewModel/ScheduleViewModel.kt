package com.sports.oscaracademy.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sports.oscaracademy.service.SessionData
import com.sports.oscaracademy.service.SessionDetailsService

class ScheduleViewModel(private val TempApplication: Application) :
    AndroidViewModel(TempApplication) {

    val service = SessionDetailsService()
    private val sessionData = service.getSpecificSchedule(TempApplication.applicationContext)

    fun getUserSchedule(): MutableLiveData<SessionData> {
        Log.e("TAG", "getUserSchedule: ${sessionData.value}")
        return sessionData
    }

    fun getUserSpecificFees(): MutableLiveData<SessionData> {
        return sessionData
    }

}