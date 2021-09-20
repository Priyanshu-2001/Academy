package com.sports.oscaracademy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sports.oscaracademy.data.SlotsData
import com.sports.oscaracademy.service.getSlots
import org.joda.time.DateTime

class Pay_playViewModel(application: Application) : AndroidViewModel(application) {

    private var selectedDate = MutableLiveData<DateTime>()
    private var selectedslots = MutableLiveData<ArrayList<SlotsData>>()
    private var totalslots: MutableLiveData<ArrayList<SlotsData>>? = null

    fun setSelectedDate(date: DateTime) {
        selectedDate.value = date
    }

    fun getSelectedDate(): MutableLiveData<DateTime> {
        return selectedDate
    }

    fun getTotalSlots(): MutableLiveData<ArrayList<SlotsData>> {
        if (totalslots == null) {
            totalslots = MutableLiveData()
//            val getSlots = getSlots()
            totalslots = getSlots().getData()
        }
        return totalslots!!
    }

    fun setSelectedSLots(arr: ArrayList<SlotsData>) {
        selectedslots.value = arr
    }

    fun getSelectedSlots(): MutableLiveData<ArrayList<SlotsData>> {
        return selectedslots
    }

}