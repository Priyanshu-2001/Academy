package com.sports.oscaracademy.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sports.oscaracademy.data.AdminBookedSlotsData
import com.sports.oscaracademy.service.AdminBookingServices

class AdminBookingListViewModel(val date: String) : ViewModel() {
    private var mainDataList = MutableLiveData<AdminBookedSlotsData>()

    init {
        setAdminBookingList()
    }

    fun setAdminBookingList() {
        mainDataList = AdminBookingServices().getAllBookedSlots(date)
    }

    fun getAdminBookingList(): MutableLiveData<AdminBookedSlotsData> {
        return mainDataList
    }
}

class AdminBookingFactory(private val date: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminBookingListViewModel::class.java)) {
            return AdminBookingListViewModel(date) as T
        }
        throw IllegalArgumentException("Unable to Access the ViewModel")
    }

}